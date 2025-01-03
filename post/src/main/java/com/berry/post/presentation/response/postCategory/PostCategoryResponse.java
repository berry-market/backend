package com.berry.post.presentation.response.postCategory;

import com.berry.common.auditor.BaseEntity;
import com.berry.post.domain.model.PostCategory;
import lombok.Getter;

@Getter
public class PostCategoryResponse extends BaseEntity {

  private Long id;
  private String categoryName;

  public PostCategoryResponse(PostCategory postCategory) {
    this.id = postCategory.getId();
    this.categoryName = postCategory.getCategoryName();
    this.setCreatedAt(postCategory.getCreatedAt());
    this.setCreatedBy(postCategory.getCreatedBy());
    this.setUpdatedAt(postCategory.getUpdatedAt());
    this.setUpdatedBy(postCategory.getUpdatedBy());
    this.setDeletedAt(postCategory.getDeletedAt());
    this.setDeletedBy(postCategory.getDeletedBy());
    this.setDeletedYN(postCategory.isDeletedYN());
  }
}
