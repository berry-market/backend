package com.berry.post.application.service.post;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.application.service.image.ImageUploadService;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductDetailsImages;
import com.berry.post.domain.model.ProductStatus;
import com.berry.post.domain.repository.post.PostRepository;
import com.berry.post.domain.repository.ProductDetailsImagesRepository;
import com.berry.post.domain.repository.post.PostRepositoryCustomImpl;
import com.berry.post.presentation.request.Post.PostCreateRequest;
import com.berry.post.presentation.response.Post.PostResponse;
import com.querydsl.core.types.Predicate;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final ProductDetailsImagesRepository productDetailsImagesRepository;
  private final ImageUploadService imageUploadService;

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

    // 임시 유저 아이디
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
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostResponse> getPosts(String keyword, String type, Long postCategoryId, String sort, Pageable pageable) {

    Page<Post> posts = postRepository.findAllAndDeletedYNFalse(keyword, type, postCategoryId, sort, pageable);

    return posts.map(post -> {
      // todo postId 마다 해당 유저의 찜 여부 확인하고 각각 response 에 추가. userId가 null 이면 로그인하지 않은 사용자이므로 isLiked = null
      Boolean isLiked = true; // 실제로는 유저에서 게시글마다 찜 여부를 호출해와야 함.
      return new PostResponse(post, isLiked);
    });
  }

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
}
