package com.berry.post.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.post.application.service.post.PostServiceImpl;
import com.berry.post.presentation.request.Post.PostCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

  private final PostServiceImpl postServiceImpl;

  // todo 게시글 생성 수정 삭제 권한 설정

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createPost(
      @Valid @RequestBody PostCreateRequest postCreateRequest) {
    postServiceImpl.createPost(postCreateRequest);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.CREATED, "게시글이 생성되었습니다."));
  }



}
