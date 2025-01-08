package com.berry.post.application.service.postCategory;

import com.berry.post.presentation.request.postCategory.PostCategoryCreateRequest;
import com.berry.post.presentation.request.postCategory.PostCategoryUpdateRequest;
import com.berry.post.presentation.response.postCategory.PostCategoryNavigationResponse;
import com.berry.post.presentation.response.postCategory.PostCategoryResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCategoryService {

  void createPostCategory(PostCategoryCreateRequest postCategoryCreateRequest);

  Page<PostCategoryResponse> getPostCategories(String keyword, Pageable pageable);

  void updatePostCategory(Long categoryId, PostCategoryUpdateRequest postCategoryUpdateRequest);

  void deletePostCategory(Long categoryId);

  List<PostCategoryNavigationResponse> getNavigations();

}
