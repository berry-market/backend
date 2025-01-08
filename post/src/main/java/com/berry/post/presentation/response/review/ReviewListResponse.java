package com.berry.post.presentation.response.review;

import com.berry.post.domain.model.Review;
import lombok.Getter;

@Getter
public class ReviewListResponse {

  private Long id;
  private Long reviewerId;
  private Long bidId;
  private Long postId;
  private String reviewContent;
  private Integer reviewScore;

  public ReviewListResponse(Review review) {
    this.id = review.getId();
    this.reviewerId = review.getReviewerId();
    this.bidId = review.getBidId();
    this.postId = review.getPostId();
    this.reviewContent = review.getReviewContent();
    this.reviewScore = review.getReviewScore();
  }
}
