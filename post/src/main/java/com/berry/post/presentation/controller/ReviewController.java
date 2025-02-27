package com.berry.post.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.common.role.RoleCheck;
import com.berry.post.application.service.review.ReviewServiceImpl;
import com.berry.post.presentation.request.review.ReviewCreateRequest;
import com.berry.post.presentation.request.review.ReviewUpdateRequest;
import com.berry.post.presentation.response.review.ReviewGradeResponse;
import com.berry.post.presentation.response.review.ReviewListResponse;
import com.berry.post.presentation.response.review.ReviewProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

  private final ReviewServiceImpl reviewService;

  @RoleCheck("MEMBER")
  @PostMapping
  public ApiResponse<Void> createReview(
      @Valid @RequestBody ReviewCreateRequest reviewCreateRequest,
      @RequestHeader(value = "X-UserId") Long userId) {
    reviewService.createReview(reviewCreateRequest, userId);
    return ApiResponse.OK(ResSuccessCode.CREATED, "리뷰가 생성되었습니다.");
  }

  @GetMapping("/by-id")
  public ApiResponse<ReviewProductResponse> getReview(
      @RequestParam Long postId) {
    ReviewProductResponse review = reviewService.getReview(postId);
    return ApiResponse.OK(ResSuccessCode.READ, review,"리뷰 단건 조회가 완료되었습니다.");
  }

  @GetMapping("/grade")
  public ApiResponse<ReviewGradeResponse> getReviewGrade(
      @RequestParam Long postId) {
    ReviewGradeResponse response = reviewService.getReviewGrade(postId);
    return ApiResponse.OK(ResSuccessCode.READ, response, "리뷰 평점 평균 조회가 완료되었습니다.");

  }

  @GetMapping
  public ApiResponse<Page<ReviewListResponse>> getReviews(
      @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
      Pageable pageable) {
    Page<ReviewListResponse> review = reviewService.getReviews(keyword, pageable);
    return ApiResponse.OK(ResSuccessCode.READ, review,"리뷰 전체 조회가 완료되었습니다.");
  }

  @PatchMapping("/{reviewId}")
  public ApiResponse<Void> updateReview(
      @Valid @RequestBody ReviewUpdateRequest updateRequest,
      @PathVariable Long reviewId,
      @RequestHeader(value = "X-UserId") Long userId,
      @RequestHeader(value = "X-Role") String role) {
    reviewService.updateReview(updateRequest, reviewId, userId, role);
    return ApiResponse.OK(ResSuccessCode.UPDATED, "리뷰가 수정되었습니다.");
  }

  @PutMapping("/{reviewId}")
  public ApiResponse<Void> deleteReview(
      @PathVariable Long reviewId,
      @RequestHeader(value = "X-UserId") Long userId,
      @RequestHeader(value = "X-Role") String role) {
    reviewService.deleteReview(reviewId, userId, role);
    return ApiResponse.OK(ResSuccessCode.DELETED, "리뷰가 삭제되었습니다.");
  }

}
