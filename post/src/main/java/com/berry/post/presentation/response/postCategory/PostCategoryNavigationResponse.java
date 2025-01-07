package com.berry.post.presentation.response.postCategory;

import com.berry.post.domain.model.PostCategory;
import lombok.Getter;

@Getter
public class PostCategoryNavigationResponse {

  private Long id;
  private String categoryName;

  public PostCategoryNavigationResponse(PostCategory postCategory) {
    this.id = postCategory.getId();
    this.categoryName = postCategory.getCategoryName();
  }
}
