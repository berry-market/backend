package com.berry.post.application.service.postCategory;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.domain.model.PostCategory;
import com.berry.post.domain.repository.PostCategoryRepository;
import com.berry.post.presentation.request.postCategory.PostCategoryCreateRequest;
import com.berry.post.presentation.request.postCategory.PostCategoryUpdateRequest;
import com.berry.post.presentation.response.postCategory.PostCategoryNavigationResponse;
import com.berry.post.presentation.response.postCategory.PostCategoryResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostCategoryServiceImpl implements PostCategoryService {

  private final PostCategoryRepository postCategoryRepository;

  @Override
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

  @Override
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

  @Override
  @Transactional
  public void updatePostCategory(Long categoryId, PostCategoryUpdateRequest postCategoryUpdateRequest) {
    if (postCategoryRepository.findByCategoryNameAndDeletedYNFalse(postCategoryUpdateRequest.getCategoryName()).isPresent()) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "이미 존재하는 카테고리입니다.");
    }

    PostCategory savedPostCategory = postCategoryRepository.findByIdAndDeletedYNFalse(categoryId)
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "존재하지 않는 카테고리 아이디입니다."));

    savedPostCategory.updateCategoryName(postCategoryUpdateRequest.getCategoryName());

    postCategoryRepository.save(savedPostCategory);
  }

  @Override
  @Transactional
  public void deletePostCategory(Long categoryId) {
    PostCategory savedPostCategory = postCategoryRepository.findByIdAndDeletedYNFalse(categoryId)
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "존재하지 않는 카테고리 아이디입니다."));

    savedPostCategory.markAsDeleted();
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(cacheNames = "postCategories", key = "'allPostCategories'")
  public List<PostCategoryNavigationResponse> getNavigations() {
    List<PostCategory> postCategories = postCategoryRepository.findAllByDeletedYNFalse();
    log.info("실제 DB 조회");
    return postCategories.stream()
        .map(PostCategoryNavigationResponse::new)
        .collect(Collectors.toList());
  }
}
