package com.berry.post.application.service.review;

import com.berry.post.presentation.request.review.ReviewCreateRequest;
import com.berry.post.presentation.response.review.ReviewListResponse;
import com.berry.post.presentation.response.review.ReviewProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

  void createReview(ReviewCreateRequest reviewCreateRequest);

  ReviewProductResponse getReview(Long postId);

  Page<ReviewListResponse> getReviews(Pageable pageable);

}
