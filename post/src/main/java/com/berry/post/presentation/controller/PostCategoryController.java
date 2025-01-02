package com.berry.post.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.post.application.service.PostCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/categories")
public class PostCategoryController {

  private final PostCategoryService postCategoryService;

  // todo auth 되면 admin 권한 확인 추가

  @PostMapping
  public ResponseEntity<ApiResponse<?>> createPostCategory(@Valid @RequestBody PostCategoryCreateRequest postCategoryCreateRequest) {
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.CREATED));
  }

}
