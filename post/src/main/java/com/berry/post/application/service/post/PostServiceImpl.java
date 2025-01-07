package com.berry.post.application.service.post;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.application.event.BidEvent;
import com.berry.post.application.event.BidEventMessage;
import com.berry.post.application.event.BidEventMessage.BidPostEvent;
import com.berry.post.application.service.consumer.PostConsumerServiceImpl;
import com.berry.post.application.service.image.ImageUploadService;
import com.berry.post.application.service.producer.PostProducerServiceImpl;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductDetailsImages;
import com.berry.post.domain.model.ProductStatus;
import com.berry.post.domain.repository.PostRepository;
import com.berry.post.domain.repository.ProductDetailsImagesRepository;
import com.berry.post.presentation.request.Post.PostCreateRequest;
import com.berry.post.presentation.request.Post.PostUpdateRequest;
import com.berry.post.presentation.response.Post.PostDetailsResponse;
import com.berry.post.presentation.response.Post.PostListResponse;
import com.berry.post.presentation.response.Post.PostServerResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final ImageUploadService imageUploadService;
  private final PostProducerServiceImpl postProducerService;
  private final PostConsumerServiceImpl postConsumerService;

  @Override
  @Transactional
  public void createPost(PostCreateRequest postCreateRequest,
      MultipartFile productImage, List<MultipartFile> productDetailsImages) throws IOException {

    LocalDateTime auctionStartedAt = postCreateRequest.getAuctionStartedAt();
    LocalDateTime auctionEndedAt = postCreateRequest.getAuctionEndedAt();
    if (auctionStartedAt.isAfter(auctionEndedAt) || auctionStartedAt.isEqual(auctionEndedAt)) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "경매 종료 날짜를 시작 날짜 이후로 조정해주세요.");
    }

    // Post 에 먼저 단일 이미지 저장
    String productImageUrl = imageUploadService.upload(productImage);

    // todo 임시 유저 아이디
    Long userId = 1L;

    // 생성 시에는 일단 상품 상태 PENDING 상태로 생성
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

    // ProductDetailsImages 에 다중 이미지 저장
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
    sendPostEventToBid(BidEvent.PostBidEvent.from(savedPost));
  }

  @Override
  @Transactional
  public Page<PostListResponse> getPosts(String keyword, String type, Long postCategoryId, String sort, Pageable pageable) {

    Page<Post> posts = postRepository.findAllAndDeletedYNFalse(keyword, type, postCategoryId, sort, pageable);

    return posts.map(post -> {
      // todo postId 마다 해당 유저의 찜 여부 확인하고 각각 response 에 추가. userId가 null 이면 로그인하지 않은 사용자이므로 isLiked = null
      Boolean isLiked = true; // 실제로는 유저에서 게시글마다 찜 여부를 호출해와야 함.
      return new PostListResponse(post, isLiked);
    });
  }

  @Override
  @Transactional
  public PostDetailsResponse getPost(Long postId) {
    Post post = postRepository.findByIdAndDeletedYNFalse(postId).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
    );

    List<String> productDetailsImages = new ArrayList<>();
    List<ProductDetailsImages> savedProductDetailsImages = productDetailsImagesRepository.findAllByPostIdAndDeletedYNFalseOrderBySequenceAsc(post.getId());
    for (ProductDetailsImages savedProductDetailsImage : savedProductDetailsImages) {
      productDetailsImages.add(savedProductDetailsImage.getProductDetailsImage());
    }

    // todo postId 마다 현재 로그인한 유저의 찜 여부 확인하고 각각 response 에 추가. userId가 null 이면 로그인하지 않은 사용자이므로 isLiked = null
    Boolean isLiked = true;
    // todo 작성자 Id로 유저 받아오고 해당 작성자의 닉네임 와서 각 response 에 추가.
    String writerNickName = "berry";

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
  public void updatePost(Long postId, PostUpdateRequest postUpdateRequest, MultipartFile productImage, List<MultipartFile> productDetailsImages)
      throws IOException {

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

    // todo 본인이 작성한 게시글인지 검증

    // Post 에 먼저 단일 이미지 저장
    if (productImage != null) {
      String productImageUrl = imageUploadService.upload(productImage);
      post.updateProductImage(productImageUrl);
    }

    // post 업데이트
    post.updateProduct(postUpdateRequest);
    postRepository.save(post);

    // 기존 다중 이미지 삭제 후 다시 저장
    if (productDetailsImages != null) {
      List<ProductDetailsImages> savedProductDetailsImages = productDetailsImagesRepository.findAllByPostIdAndDeletedYNFalseOrderBySequenceAsc(post.getId());
      productDetailsImagesRepository.deleteAll(savedProductDetailsImages);

      for (int i = 0; i < productDetailsImages.size(); i++) {
        MultipartFile detailsImage = productDetailsImages.get(i);
        String productDetailsImageUrl = imageUploadService.upload(detailsImage);
        ProductDetailsImages productDetailsImage = ProductDetailsImages.builder()
            .postId(post.getId())
            .productDetailsImage(productDetailsImageUrl)
            .sequence(i)
            .build();
        productDetailsImagesRepository.save(productDetailsImage);
      }
    }
  }

  @Override
  @Transactional
  public void deletePost(Long postId) {
    Post post = postRepository.findByIdAndDeletedYNFalse(postId).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
    );

    // todo 본인이 작성한 게시글인지 검증

    // post 삭제 처리
    post.markAsDeleted();
    postRepository.save(post);

    // productDetailsImage 삭제 처리
    List<ProductDetailsImages> savedProductDetailsImages = productDetailsImagesRepository.findAllByPostIdAndDeletedYNFalseOrderBySequenceAsc(post.getId());
    for (ProductDetailsImages productDetailsImage : savedProductDetailsImages) {
      productDetailsImage.markAsDeleted();
      productDetailsImagesRepository.save(productDetailsImage);
    }
  }

  // ------------------------


  // post 상태 자동 업데이트
  @Scheduled(fixedRate = 60000)  // 1분
  public void updateProductStatus() {
    LocalDateTime now = LocalDateTime.now();

    List<Post> productStatusToStarts =
        postRepository.findAllByAuctionStartedAtBeforeAndProductStatusNotAndDeletedYNFalse(now, ProductStatus.ACTIVE);
    for (Post productStatusToStart : productStatusToStarts) {
      productStatusToStart.updateProductStatus(ProductStatus.ACTIVE);
      postRepository.save(productStatusToStart);
    }

    List<Post> productStatusToCloses =
        postRepository.findAllByAuctionEndedAtBeforeAndProductStatusNotAndDeletedYNFalse(now, ProductStatus.CLOSED);
    for (Post productStatusToClose : productStatusToCloses) {
      productStatusToClose.updateProductStatus(ProductStatus.CLOSED);
      postRepository.save(productStatusToClose);
    }
  }

  private void sendPostEventToBid(BidEvent.PostBidEvent event) {
    postProducerService.sendPostEvent(event);
  }
}
