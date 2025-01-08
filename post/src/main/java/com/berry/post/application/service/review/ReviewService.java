package com.berry.post.application.service.review;

import com.berry.post.presentation.request.Post.ReviewCreateRequest;

public interface ReviewService {

  void createReview(ReviewCreateRequest reviewCreateRequest);

}
