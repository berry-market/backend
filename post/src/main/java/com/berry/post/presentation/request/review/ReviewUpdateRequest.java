package com.berry.post.presentation.request.review;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewUpdateRequest {

  private String reviewContent;

  private Integer reviewScore;

}
