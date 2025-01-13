package com.berry.post.application.service.review;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.application.dto.GetInternalUserResponse;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.Review;
import com.berry.post.domain.repository.PostRepository;
import com.berry.post.domain.repository.ReviewRepository;
import com.berry.post.infrastructure.client.UserClient;
import com.berry.post.presentation.request.review.ReviewCreateRequest;
import com.berry.post.presentation.request.review.ReviewUpdateRequest;
import com.berry.post.presentation.response.review.ReviewGradeResponse;
import com.berry.post.presentation.response.review.ReviewListResponse;
import com.berry.post.presentation.response.review.ReviewProductResponse;
import feign.FeignException.FeignClientException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
  private final UserClient userClient;

  @Override
  @Transactional
  public void createReview(ReviewCreateRequest reviewCreateRequest, Long userId) {

    if (reviewRepository.findByReviewerIdAndPostIdAndDeletedYNFalse(userId,
        reviewCreateRequest.getPostId()).isPresent()) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "이미 리뷰 작성이 완료된 상품입니다.");
    }

    Review review = Review.builder()
        .reviewerId(userId)
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

    GetInternalUserResponse user;
    try {
      user = userClient.getInternalUserById(review.getReviewerId()).getData();
    } catch (FeignClientException e) {
      throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
          "User Service: " + e.getMessage());
    }

    String nickname = user.nickname();

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

  @Override
  @Transactional
  public void updateReview(ReviewUpdateRequest updateRequest, Long reviewId, Long userId, String role) {

    if (!role.equals("MEMBER") && !role.equals("ADMIN")) {
      throw new CustomApiException(ResErrorCode.FORBIDDEN);
    }

    Review savedReview = reviewRepository.findByIdAndDeletedYNFalse(reviewId).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다.")
    );

    if (!Objects.equals(savedReview.getReviewerId(), userId) && !role.equals("ADMIN")) {
      throw new CustomApiException(ResErrorCode.FORBIDDEN, "권한이 없습니다.");
    }

    savedReview.updateReview(updateRequest);
  }

  @Override
  @Transactional
  public void deleteReview(Long reviewId, Long userId, String role) {

    if (!role.equals("MEMBER") && !role.equals("ADMIN")) {
      throw new CustomApiException(ResErrorCode.FORBIDDEN);
    }

    Review savedReview = reviewRepository.findByIdAndDeletedYNFalse(reviewId).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다.")
    );

    if (!Objects.equals(savedReview.getReviewerId(), userId) && !role.equals("ADMIN")) {
      throw new CustomApiException(ResErrorCode.FORBIDDEN, "권한이 없습니다.");
    }

    savedReview.markAsDeleted();
  }
}
