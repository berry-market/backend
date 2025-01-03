package com.berry.post.application.service.post;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductStatus;
import com.berry.post.presentation.request.Post.PostCreateRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  @Override
  @Transactional
  public void createPost(PostCreateRequest postCreateRequest) {
    // 경매 종료 날짜가 시작 날짜보다 미래인지 확인
    LocalDateTime auctionStartedAt = postCreateRequest.getAuctionStartedAt();
    LocalDateTime auctionEndedAt = postCreateRequest.getAuctionEndedAt();
    if (auctionStartedAt.isAfter(auctionEndedAt) || auctionStartedAt.isEqual(auctionEndedAt)) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "경매 종료 날짜를 시작 날짜 이후로 조정해주세요.");
    }

    // 이미지 받아서 각각 저장
    MultipartFile productImage = postCreateRequest.getProductImage();
    List<MultipartFile> productDetailsImages = postCreateRequest.getProductDetailsImages();

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
        .deliveryMethod(postCreateRequest.get)


        .build();

    // 스케줄러 시작해서 시작 날짜, 마감 날짜에 맞게 상품 상태 변하도록 설정

    // DB에 저장
  }

}
