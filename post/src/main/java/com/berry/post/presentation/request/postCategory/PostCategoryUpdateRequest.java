package com.berry.post.presentation.request.postCategory;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostCategoryUpdateRequest {

  @NotBlank(message = "카테고리명은 필수입니다.")
  private String categoryName;

}
