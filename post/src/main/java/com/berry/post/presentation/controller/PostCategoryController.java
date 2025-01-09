package com.berry.post.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.common.role.RoleCheck;
import com.berry.post.application.service.postCategory.PostCategoryServiceImpl;
import com.berry.post.presentation.request.postCategory.PostCategoryCreateRequest;
import com.berry.post.presentation.request.postCategory.PostCategoryUpdateRequest;
import com.berry.post.presentation.response.postCategory.PostCategoryNavigationResponse;
import com.berry.post.presentation.response.postCategory.PostCategoryResponse;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostCategoryController {

  private final PostCategoryServiceImpl postCategoryServiceImpl;

  // todo auth 되면 admin 권한 확인 추가

  @RoleCheck("ADMIN")
  @PostMapping("/admin/v1/categories")
  public ApiResponse<Void> createPostCategory(
      @Valid @RequestBody PostCategoryCreateRequest postCategoryCreateRequest) {
    postCategoryServiceImpl.createPostCategory(postCategoryCreateRequest);
    return ApiResponse.OK(ResSuccessCode.CREATED, "게시글 카테고리가 생성되었습니다.");
  }

  @RoleCheck("ADMIN")
  @GetMapping("/admin/v1/categories")
  public ApiResponse<Page<PostCategoryResponse>> getPostCategories(
      @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword, Pageable pageable) {
    Page<PostCategoryResponse> postCategories = postCategoryServiceImpl.getPostCategories(keyword, pageable);
    return ApiResponse.OK(ResSuccessCode.READ, postCategories, "게시글 카테고리가 전체 조회되었습니다. ");
  }

  @RoleCheck("ADMIN")
  @PatchMapping("/admin/v1/categories/{categoryId}")
  public ApiResponse<Void> updatePostCategory(
      @PathVariable Long categoryId,
      @Valid @RequestBody PostCategoryUpdateRequest postCategoryUpdateRequest) {
    postCategoryServiceImpl.updatePostCategory(categoryId, postCategoryUpdateRequest);
    return ApiResponse.OK(ResSuccessCode.UPDATED, "게시글 카테고리가 수정되었습니다.");
  }

  @RoleCheck("ADMIN")
  @PutMapping("/admin/v1/categories/{categoryId}")
  public ApiResponse<Void> deletePostCategory(@PathVariable Long categoryId) {
    postCategoryServiceImpl.deletePostCategory(categoryId);
    return ApiResponse.OK(ResSuccessCode.DELETED, "게시글 카테고리가 삭제되었습니다.");
  }

  @GetMapping("/api/v1/categories")
  public ApiResponse<List<PostCategoryNavigationResponse>> getNavigations() {
    List<PostCategoryNavigationResponse> navigations = postCategoryServiceImpl.getNavigations();
    return ApiResponse.OK(ResSuccessCode.READ, navigations,"게시글 카테고리가 전체 조회되었습니다.");
  }

}
