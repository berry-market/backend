package com.berry.post.application.service.review;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.Review;
import com.berry.post.domain.repository.PostRepository;
import com.berry.post.domain.repository.ReviewRepository;
import com.berry.post.presentation.request.review.ReviewCreateRequest;
import com.berry.post.presentation.response.review.ReviewGradeResponse;
import com.berry.post.presentation.response.review.ReviewListResponse;
import com.berry.post.presentation.response.review.ReviewProductResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    Review review = reviewRepository.findByPostIdAndDeletedYNFalse(postId);
    if (review == null) {
      throw new CustomApiException(ResErrorCode.NOT_FOUND, "해당 상품에 대한 리뷰가 존재하지 않습니다.");
    }

    Post post = postRepository.findByIdAndDeletedYNFalse(review.getPostId()).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
    );

    // todo reviewerId 로 리뷰어 닉네임 가져오기
    String nickname = "임시 닉네임";

    return new ReviewProductResponse(review, post.getProductName(), nickname);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ReviewListResponse> getReviews(String keyword, Pageable pageable) {

    int size = pageable.getPageSize();
    Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.by(
        Sort.Order.desc("createdAt")
    );
    pageable = PageRequest.of(pageable.getPageNumber(), size, sort);

    Page<Review> reviews;
    if (keyword.isEmpty()) {
      reviews = reviewRepository.findAllByDeletedYNFalse(pageable);
    } else {
      reviews = reviewRepository.findAllByReviewContentAndDeletedYNFalse(pageable, keyword);
    }

    return reviews.map(ReviewListResponse::new);
  }

  @Override
  @Transactional(readOnly = true)
  public ReviewGradeResponse getReviewGrade(Long postId) {

    Post post = postRepository.findByIdAndDeletedYNFalse(postId).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
    );

    // 판매자 Id로 판매자가 올린 상품들 전부 조회
    List<Post> products = postRepository.findAllByWriterIdAndDeletedYNFalse(post.getWriterId());

    // 판매자가 올린 상품의 상품 아이디로 리뷰 조회한 후 점수 저장
    List<Integer> reviewScore = new ArrayList<>();
    for (Post product : products) {
      Review review = reviewRepository.findByPostIdAndDeletedYNFalse(product.getId());
      if (review != null) {
        reviewScore.add(review.getReviewScore());
      }
    }

    Double totalScore = reviewScore.stream()
        .mapToDouble(Double::valueOf)
        .average()
        .orElse(0.0);

    return new ReviewGradeResponse(totalScore);
  }
}
