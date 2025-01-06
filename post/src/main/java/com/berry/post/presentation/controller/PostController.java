package com.berry.post.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.post.application.service.post.PostServiceImpl;
import com.berry.post.presentation.request.Post.PostCreateRequest;
import com.berry.post.presentation.response.Post.PostDetailsResponse;
import com.berry.post.presentation.response.Post.PostListResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping
  public ApiResponse<Page<PostListResponse>> getPosts(
      @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
      @RequestParam(name = "type", required = false, defaultValue = "") String type,
      @RequestParam(name = "categoryId", required = false, defaultValue = "") Long postCategoryId,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sort,
      Pageable pageable
  ) {
    Page<PostListResponse> posts = postServiceImpl.getPosts(keyword, type, postCategoryId, sort, pageable);
    return ApiResponse.OK(ResSuccessCode.READ, posts,"게시글이 전체 조회되었습니다. ");
  }

  @GetMapping("/{postId}")
  public ResponseEntity<ApiResponse<PostDetailsResponse>> getPost(@PathVariable Long postId) {
    PostDetailsResponse post = postServiceImpl.getPost(postId);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, post, "게시글이 단건 조회되었습니다."));
  }


}
