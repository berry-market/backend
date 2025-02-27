package com.berry.post.application.service.post;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.application.dto.GetInternalUserResponse;
import com.berry.post.application.model.event.PostEvent.Status;
import com.berry.post.application.service.image.ImageUploadService;
import com.berry.post.application.service.producer.PostProducerServiceImpl;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductDetailsImages;
import com.berry.post.domain.model.ProductStatus;
import com.berry.post.domain.model.Review;
import com.berry.post.domain.repository.LikeRepository;
import com.berry.post.domain.repository.PostRepository;
import com.berry.post.domain.repository.ProductDetailsImagesRepository;
import com.berry.post.domain.repository.ReviewRepository;
import com.berry.post.infrastructure.client.UserService;
import com.berry.post.presentation.request.Post.PostCreateRequest;
import com.berry.post.presentation.request.Post.PostUpdateRequest;
import com.berry.post.presentation.response.Post.PostDetailsResponse;
import com.berry.post.presentation.response.Post.PostListResponse;
import com.berry.post.presentation.response.Post.PostServerResponse;
import feign.FeignException.FeignClientException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final ProductDetailsImagesRepository productDetailsImagesRepository;
  private final ReviewRepository reviewRepository;
  private final LikeRepository likeRepository;
  private final ImageUploadService imageUploadService;
  private final PostProducerServiceImpl postProducerService;
  private final UserService userService;

  @Override
  @Transactional
  public void createPost(PostCreateRequest postCreateRequest,
      MultipartFile productImage, List<MultipartFile> productDetailsImages, Long userId,
      String role) throws IOException {

    if (!role.equals("MEMBER")) {
      throw new CustomApiException(ResErrorCode.FORBIDDEN, "권한이 없습니다.");
    }

    LocalDateTime auctionStartedAt = postCreateRequest.getAuctionStartedAt();
    LocalDateTime auctionEndedAt = postCreateRequest.getAuctionEndedAt();
    if (auctionStartedAt.isAfter(auctionEndedAt) || auctionStartedAt.isEqual(auctionEndedAt)) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "경매 종료 날짜를 시작 날짜 이후로 조정해주세요.");
    }

    String productImageUrl = imageUploadService.upload(productImage);

    Post post = Post.builder()
        .postCategoryId(postCreateRequest.getPostCategoryId())
        .writerId(userId)
        .productName(postCreateRequest.getProductName())
        .productContent(postCreateRequest.getProductContent())
        .immediatePrice(postCreateRequest.getImmediatePrice())
        .startedPrice(postCreateRequest.getStartedPrice())
        .auctionStartedAt(auctionStartedAt)
        .auctionEndedAt(auctionEndedAt)
        .productStatus(ProductStatus.PENDING)
        .deliveryMethod(postCreateRequest.getDeliveryMethod())
        .deliveryFee(postCreateRequest.getDeliveryFee())
        .productImage(productImageUrl)
        .likeCount(0)
        .viewCount(0)
        .build();
    Post savedPost = postRepository.save(post);

    saveProductDetailsImages(productDetailsImages, savedPost);
  }

  @Override
  @Transactional
  @Cacheable(cacheNames = "posts", key = "#keyword + '-' + #type + '-' + #postCategoryId + '-' + #writerId + '-' + #sort + '-' + #pageable + '-' + #userId")
  public Page<PostListResponse> getPosts(String keyword, String type, Long postCategoryId, Long writerId,
      String sort, Pageable pageable, Long userId) {

    Page<Post> posts = postRepository.findAllAndDeletedYNFalse(keyword, type, postCategoryId, writerId, sort, pageable, userId);
    return posts.map(post -> {
      Boolean isLiked = likeRepository.findByUserIdAndPostId(userId, post.getId()).isPresent();
      return new PostListResponse(post, isLiked);
    });
  }

  @Override
  @Transactional
  public PostDetailsResponse getPost(Long postId, Long userId) {
    Post post = postRepository.findByIdAndDeletedYNFalse(postId).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
    );

    List<String> productDetailsImages = new ArrayList<>();
    List<ProductDetailsImages> savedProductDetailsImages = productDetailsImagesRepository.findAllByPostIdAndDeletedYNFalseOrderBySequenceAsc(
        post.getId());
    for (ProductDetailsImages savedProductDetailsImage : savedProductDetailsImages) {
      productDetailsImages.add(savedProductDetailsImage.getProductDetailsImage());
    }

    Boolean isLiked = likeRepository.findByUserIdAndPostId(userId, post.getId()).isPresent();

    GetInternalUserResponse user;
    try {
      user = userService.getInternalUserByIdAsync(post.getWriterId()).get().getData();
    } catch (FeignClientException | InterruptedException | ExecutionException e) {
      throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
          "User Service: " + e.getMessage());
    }

    String writerNickName = user.nickname();

    post.updateViewCount();
    postRepository.save(post);

    return new PostDetailsResponse(post, productDetailsImages, isLiked, writerNickName);
  }

  @Override
  @Transactional
  public PostServerResponse getServerPost(Long postId) {
    Post post = postRepository.findByIdAndDeletedYNFalse(postId).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
    );
    return new PostServerResponse(post);
  }

  @Override
  @Transactional
  public void updatePost(Long postId, PostUpdateRequest postUpdateRequest,
      MultipartFile productImage, List<MultipartFile> productDetailsImages, Long userId,
      String role)
      throws IOException {

    if (!role.equals("MEMBER") && !role.equals("ADMIN")) {
      throw new CustomApiException(ResErrorCode.FORBIDDEN);
    }

    LocalDateTime auctionStartedAt = postUpdateRequest.getAuctionStartedAt();
    LocalDateTime auctionEndedAt = postUpdateRequest.getAuctionEndedAt();
    if (auctionStartedAt != null && auctionEndedAt != null) {
      if (auctionStartedAt.isAfter(auctionEndedAt) || auctionStartedAt.isEqual(auctionEndedAt)) {
        throw new CustomApiException(ResErrorCode.BAD_REQUEST, "경매 종료 날짜를 시작 날짜 이후로 조정해주세요.");
      }
    }

    Post post = postRepository.findByIdAndDeletedYNFalse(postId).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
    );

    if (!Objects.equals(post.getWriterId(), userId) && !role.equals("ADMIN")) {
      throw new CustomApiException(ResErrorCode.FORBIDDEN, "권한이 없습니다.");
    }

    if (productImage != null) {
      String productImageUrl = imageUploadService.upload(productImage);
      post.updateProductImage(productImageUrl);
    }

    post.updateProduct(postUpdateRequest);

    if (productDetailsImages != null) {
      List<ProductDetailsImages> savedProductDetailsImages = productDetailsImagesRepository.findAllByPostIdAndDeletedYNFalseOrderBySequenceAsc(
          post.getId());
      productDetailsImagesRepository.deleteAll(savedProductDetailsImages);

      saveProductDetailsImages(productDetailsImages, post);
    }
  }

  @Override
  @Transactional
  public void deletePost(Long postId, Long userId, String role) {

    if (!role.equals("MEMBER") && !role.equals("ADMIN")) {
      throw new CustomApiException(ResErrorCode.FORBIDDEN);
    }

    Post post = postRepository.findByIdAndDeletedYNFalse(postId).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
    );

    if (!Objects.equals(post.getWriterId(), userId) && !role.equals("ADMIN")) {
      throw new CustomApiException(ResErrorCode.FORBIDDEN, "권한이 없습니다.");
    }

    post.markAsDeleted();

    List<ProductDetailsImages> savedProductDetailsImages = productDetailsImagesRepository.findAllByPostIdAndDeletedYNFalseOrderBySequenceAsc(
        post.getId());
    for (ProductDetailsImages productDetailsImage : savedProductDetailsImages) {
      productDetailsImage.markAsDeleted();
    }

    Review review = reviewRepository.findByPostIdAndDeletedYNFalse(post.getId());
    if (review != null) {
      review.markAsDeleted();
    }
  }

  private void saveProductDetailsImages(List<MultipartFile> productDetailsImages, Post savedPost)
      throws IOException {
    for (int i = 0; i < productDetailsImages.size(); i++) {
      MultipartFile detailsImage = productDetailsImages.get(i);
      String productDetailsImageUrl = imageUploadService.upload(detailsImage);
      ProductDetailsImages productDetailsImage = ProductDetailsImages.builder()
          .postId(savedPost.getId())
          .productDetailsImage(productDetailsImageUrl)
          .sequence(i)
          .build();
      productDetailsImagesRepository.save(productDetailsImage);
    }
  }

  // post 상태 자동 업데이트
  @Scheduled(fixedRate = 60000)  // 1분
  public void updateProductStatus() {
    LocalDateTime now = LocalDateTime.now();

    List<Post> productStatusToStarts =
        postRepository.findAllByAuctionStartedAtBeforeAndProductStatusAndDeletedYNFalse(now,
            ProductStatus.PENDING);
    for (Post productStatusToStart : productStatusToStarts) {
      productStatusToStart.updateProductStatus(ProductStatus.ACTIVE);
      Post savedPost = postRepository.save(productStatusToStart);
      sendPostActiveEventToBid(Status.from(savedPost));
    }

    List<Post> productStatusToCloses =
        postRepository.findAllByAuctionEndedAtBeforeAndProductStatusNotAndDeletedYNFalse(now,
            ProductStatus.CLOSED);
    for (Post productStatusToClose : productStatusToCloses) {
      productStatusToClose.updateProductStatus(ProductStatus.CLOSED);
      Post savedPost = postRepository.save(productStatusToClose);
      sendPostCloseEventToBid(Status.from(savedPost));
    }
  }

  private void sendPostActiveEventToBid(Status event) {
    postProducerService.sendPostCloseEvent(event);
  }

  private void sendPostCloseEventToBid(Status event) {
    postProducerService.sendPostActiveEvent(event);
  }
}
