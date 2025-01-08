package com.berry.post.presentation.response.review;

import lombok.Getter;

@Getter
public class ReviewGradeResponse {

  private Double grade;

  public ReviewGradeResponse(Double grade) {
    this.grade = grade;
  }
}
