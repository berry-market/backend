package com.berry.post.application.service.post;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.application.service.image.ImageUploadService;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductDetailsImages;
import com.berry.post.domain.model.ProductStatus;
import com.berry.post.domain.repository.PostRepository;
import com.berry.post.domain.repository.ProductDetailsImagesRepository;
import com.berry.post.presentation.request.Post.PostCreateRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
