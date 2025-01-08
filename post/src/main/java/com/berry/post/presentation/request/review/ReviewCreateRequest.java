package com.berry.post.presentation.request.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewCreateRequest {

  @NotNull
  private Long bidId;

  @NotNull
  private Long postId;

  @NotBlank(message = "리뷰 내용은 필수입니다.")
  private String reviewContent;

  @NotNull(message = "리뷰 평점은 필수입니다.")
  private Integer reviewScore;
}
