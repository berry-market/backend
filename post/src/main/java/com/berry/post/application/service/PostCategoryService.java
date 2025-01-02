package com.berry.post.application.service;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.domain.model.PostCategory;
import com.berry.post.domain.repository.PostCategoryRepository;
import com.berry.post.presentation.request.postCategory.PostCategoryCreateRequest;
import com.berry.post.presentation.response.postCategory.PostCategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostCategoryService {

  private final PostCategoryRepository postCategoryRepository;

  @Transactional
  public void createPostCategory(PostCategoryCreateRequest postCategoryCreateRequest) {
    if (postCategoryRepository.findByCategoryNameAndDeletedYNFalse(postCategoryCreateRequest.getCategoryName()).isPresent()) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "이미 존재하는 카테고리입니다.");
    }
    PostCategory postCategory = PostCategory.builder()
        .categoryName(postCategoryCreateRequest.getCategoryName())
        .build();

    postCategoryRepository.save(postCategory);
  }

  @Transactional(readOnly = true)
  public Page<PostCategoryResponse> getPostCategories(String keyword, Pageable pageable) {

    int size = pageable.getPageSize();
    Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.by(
        Sort.Order.desc("createdAt")
    );
    pageable = PageRequest.of(pageable.getPageNumber(), size, sort);

    Page<PostCategory> categories;
    if (keyword.isEmpty()) {
      categories = postCategoryRepository.findAllByDeletedYNFalse(pageable);

    } else {
      categories = postCategoryRepository.findAllByCategoryNameAndDeletedYNFalse(pageable, keyword);
    }

    return categories.map(PostCategoryResponse::new);
  }
}
