package com.berry.post.application.service.review;

import com.berry.post.presentation.request.review.ReviewCreateRequest;
import com.berry.post.presentation.response.review.ReviewProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

  void createReview(ReviewCreateRequest reviewCreateRequest);

  Page<ReviewProductResponse> getReview(Long postId, Pageable pageable);

}
