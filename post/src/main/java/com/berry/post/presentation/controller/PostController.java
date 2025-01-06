package com.berry.post.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.post.application.service.post.PostServiceImpl;
import com.berry.post.presentation.request.Post.PostCreateRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

  private final PostServiceImpl postServiceImpl;

  // todo 게시글 생성 수정 삭제 권한 설정

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createPost(
      @Valid @RequestPart(value = "postCreateRequest") PostCreateRequest postCreateRequest,
      @RequestPart(value = "productImage") MultipartFile productImage,
      @RequestPart(value = "productDetailsImages")List<MultipartFile> productDetailsImages
      ) throws IOException {
    postServiceImpl.createPost(postCreateRequest, productImage, productDetailsImages);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.CREATED, "게시글이 생성되었습니다."));
  }



}
