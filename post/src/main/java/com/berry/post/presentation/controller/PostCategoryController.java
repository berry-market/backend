package com.berry.post.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.post.application.service.PostCategoryService;
import com.berry.post.presentation.request.postCategory.PostCategoryCreateRequest;
import com.berry.post.presentation.request.postCategory.PostCategoryUpdateRequest;
import com.berry.post.presentation.response.postCategory.PostCategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/categories")
public class PostCategoryController {

  private final PostCategoryService postCategoryService;

  // todo auth 되면 admin 권한 확인 추가

  @PostMapping
  public ResponseEntity<ApiResponse<?>> createPostCategory(
      @Valid @RequestBody PostCategoryCreateRequest postCategoryCreateRequest) {
    postCategoryService.createPostCategory(postCategoryCreateRequest);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.CREATED, "게시글 카테고리가 생성되었습니다."));
  }

  @GetMapping
  public ApiResponse<Page<PostCategoryResponse>> getPostCategories(
      @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword, Pageable pageable) {
    Page<PostCategoryResponse> postCategories = postCategoryService.getPostCategories(keyword, pageable);
    return ApiResponse.OK(ResSuccessCode.READ, postCategories, "게시글 카테고리가 전체 조회되었습니다. ");
  }

  @PatchMapping("/{categoryId}")
  public ResponseEntity<ApiResponse<Void>> updatePostCategory(
      @PathVariable Long categoryId,
      @Valid @RequestBody PostCategoryUpdateRequest postCategoryUpdateRequest) {
    postCategoryService.updatePostCategory(categoryId, postCategoryUpdateRequest);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.UPDATED, "게시글 카테고리가 수정되었습니다."));
  }

  @PutMapping("/{categoryId}")
  public ResponseEntity<ApiResponse<Void>> deletePostCategory(@PathVariable Long categoryId) {
    postCategoryService.deletePostCategory(categoryId);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.DELETED, "게시글 카테고리가 삭제되었습니다."));
  }

}
