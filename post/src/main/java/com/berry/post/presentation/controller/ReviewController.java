package com.berry.post.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.post.application.service.review.ReviewServiceImpl;
import com.berry.post.presentation.request.review.ReviewCreateRequest;
import com.berry.post.presentation.response.review.ReviewProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

  private final ReviewServiceImpl reviewService;

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createReview(
      @Valid @RequestBody ReviewCreateRequest reviewCreateRequest) {
    reviewService.createReview(reviewCreateRequest);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.CREATED, "리뷰가 생성되었습니다."));
  }

  @GetMapping
  public ApiResponse<Page<ReviewProductResponse>> getReview(
      @RequestParam(name = "postId", required = false, defaultValue = "") Long postId,
      Pageable pageable) {
    Page<ReviewProductResponse> review = reviewService.getReview(postId, pageable);
    return ApiResponse.OK(ResSuccessCode.READ, review,"리뷰 단건 조회가 완료되었습니다.");
  }

}
