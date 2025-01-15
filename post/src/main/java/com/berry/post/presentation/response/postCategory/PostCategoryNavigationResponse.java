package com.berry.post.presentation.response.postCategory;

import com.berry.post.domain.model.PostCategory;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class PostCategoryNavigationResponse implements Serializable {

  private Long id;
  private String categoryName;

  public PostCategoryNavigationResponse(PostCategory postCategory) {
    this.id = postCategory.getId();
    this.categoryName = postCategory.getCategoryName();
  }
}
