package com.berry.post.presentation.response.review;

import com.berry.post.domain.model.Review;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ReviewProductResponse {
  private String productName;
  private String reviewContent;
  private Integer reviewScore;
  private String nickname;
  private LocalDateTime createdAt;

  public ReviewProductResponse(Review review, String productName, String nickname) {
    this.productName = productName;
    this.reviewContent = review.getReviewContent();
    this.reviewScore = review.getReviewScore();
    this.nickname = nickname;
    this.createdAt = review.getCreatedAt();
  }
}
