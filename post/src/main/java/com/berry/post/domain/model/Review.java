package com.berry.post.domain.model;

import com.berry.common.auditor.BaseEntity;
import com.berry.post.presentation.request.review.ReviewUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class Review extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long reviewerId;

  @Column(nullable = false)
  private Long bidId;

  @Column(nullable = false)
  private Long postId;

  @Column(nullable = false)
  private String reviewContent;

  @Column(nullable = false)
  private Integer reviewScore;

  public void updateReview(ReviewUpdateRequest updateRequest) {
    this.reviewContent = updateRequest.getReviewContent();
    this.reviewScore = updateRequest.getReviewScore();
  }
}
