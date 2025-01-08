package com.berry.post.application.service.review;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.Review;
import com.berry.post.domain.repository.PostRepository;
import com.berry.post.domain.repository.ReviewRepository;
import com.berry.post.presentation.request.review.ReviewCreateRequest;
import com.berry.post.presentation.response.review.ReviewListResponse;
import com.berry.post.presentation.response.review.ReviewProductResponse;
import java.util.Optional;
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
  public ReviewProductResponse getReview(Long postId) {
    Review review = reviewRepository.findByPostIdAndDeletedYNFalse(postId).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 상품에 대한 리뷰가 존재하지 않습니다.")
    );

    // todo postId로 상품명 가져오기
    Post post = postRepository.findByIdAndDeletedYNFalse(review.getPostId()).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
    );

    // todo reviewerId 로 리뷰어 닉네임 가져오기
    String nickname = "임시 닉네임";

    return new ReviewProductResponse(review, post.getProductName(), nickname);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ReviewListResponse> getReviews(Pageable pageable) {
    Page<Review> reviews = reviewRepository.findAllAndDeletedYNFalse(pageable);

    if (reviews.isEmpty()) {
      throw new CustomApiException(ResErrorCode.NOT_FOUND, "리뷰가 존재하지 않습니다.");
    }

    return null;
  }

}
