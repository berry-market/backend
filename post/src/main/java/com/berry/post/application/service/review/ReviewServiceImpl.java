package com.berry.post.application.service.review;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.Review;
import com.berry.post.domain.repository.PostRepository;
import com.berry.post.domain.repository.ReviewRepository;
import com.berry.post.presentation.request.review.ReviewCreateRequest;
import com.berry.post.presentation.response.review.ReviewProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;
  private final PostRepository postRepository;

  @Override
  @Transactional
  public void createReview(ReviewCreateRequest reviewCreateRequest) {

    // todo 리뷰어 아이디는 현재 로그인 중인 유저의 아이디에서 가져오기
    Long reviewerId = 1L;

    if (reviewRepository.findByReviewerIdAndPostIdAndDeletedYNFalse(reviewerId,
        reviewCreateRequest.getPostId()).isPresent()) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "이미 리뷰 작성이 완료된 상품입니다.");
    }

    Review review = Review.builder()
        .reviewerId(reviewerId)
        .bidId(reviewCreateRequest.getBidId())
        .postId(reviewCreateRequest.getPostId())
        .reviewContent(reviewCreateRequest.getReviewContent())
        .reviewScore(reviewCreateRequest.getReviewScore())
        .build();
    reviewRepository.save(review);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ReviewProductResponse> getReview(Long postId, Pageable pageable) {
    Page<Review> reviews = reviewRepository.findByPostIdAndDeletedYNFalse(postId, pageable);

    if (reviews.isEmpty()) {
      throw new CustomApiException(ResErrorCode.NOT_FOUND, "리뷰가 존재하지 않습니다.");
    }

    Post post = postRepository.findByIdAndDeletedYNFalse(postId).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."));
    String productName = post.getProductName();

    return reviews.map(r -> {
      // todo reviewerId 로 유저 닉네임 검색
      String nickname = "닉네임";
      return new ReviewProductResponse(r, productName, nickname);
    });
  }
}
