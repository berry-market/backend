package com.berry.post.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.common.role.RoleCheck;
import com.berry.post.application.service.post.PostServiceImpl;
import com.berry.post.presentation.request.Post.PostCreateRequest;
import com.berry.post.presentation.request.Post.PostUpdateRequest;
import com.berry.post.presentation.response.Post.PostDetailsResponse;
import com.berry.post.presentation.response.Post.PostListResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

  private final PostServiceImpl postServiceImpl;

  @PostMapping
  public ApiResponse<Void> createPost(
      @Valid @RequestPart(value = "postCreateRequest") PostCreateRequest postCreateRequest,
      @RequestHeader(value = "X-UserId") Long userId,
      @RequestHeader(value = "X-Role") String role,
      @RequestPart(value = "productImage", required = false) MultipartFile productImage,
      @RequestPart(value = "productDetailsImages", required = false)List<MultipartFile> productDetailsImages
      ) throws IOException {
    postServiceImpl.createPost(postCreateRequest, productImage, productDetailsImages, userId, role);
    return ApiResponse.OK(ResSuccessCode.CREATED, "게시글이 생성되었습니다.");
  }

  @GetMapping
  public ApiResponse<Page<PostListResponse>> getPosts(
      @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
      @RequestParam(name = "type", required = false, defaultValue = "") String type,
      @RequestParam(name = "categoryId", required = false, defaultValue = "") Long postCategoryId,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sort,
      @RequestHeader(value = "X-UserId", required = false) Long userId,
      Pageable pageable
  ) {
    Page<PostListResponse> posts = postServiceImpl.getPosts(keyword, type, postCategoryId, sort, pageable, userId);
    return ApiResponse.OK(ResSuccessCode.READ, posts,"게시글이 전체 조회되었습니다.");
  }

  @GetMapping("/{postId}")
  public ApiResponse<PostDetailsResponse> getPost(
      @PathVariable Long postId,
      @RequestHeader(value = "X-UserId", required = false) Long userId
  ) {
    PostDetailsResponse post = postServiceImpl.getPost(postId, userId);
    return ApiResponse.OK(ResSuccessCode.READ, post, "게시글이 단건 조회되었습니다.");
  }

  @PatchMapping("/{postId}")
  public ApiResponse<Void> updatePost(
      @PathVariable Long postId,
      @Valid @RequestPart(value = "postUpdateRequest") PostUpdateRequest postUpdateRequest,
      @RequestPart(value = "productImage", required = false) MultipartFile productImage,
      @RequestPart(value = "productDetailsImages", required = false) List<MultipartFile> productDetailsImages,
      @RequestHeader(value = "X-UserId") Long userId,
      @RequestHeader(value = "X-Role") String role
      ) throws IOException {
    postServiceImpl.updatePost(postId, postUpdateRequest, productImage, productDetailsImages, userId, role);
    return ApiResponse.OK(ResSuccessCode.UPDATED, "게시글이 수정되었습니다.");
  }

  @PutMapping("/{postId}")
  public ApiResponse<Void> deletePost(
      @PathVariable Long postId,
      @RequestHeader(value = "X-UserId") Long userId,
      @RequestHeader(value = "X-Role") String role) {
    postServiceImpl.deletePost(postId, userId, role);
    return ApiResponse.OK(ResSuccessCode.DELETED, "게시글이 삭제되었습니다.");
  }


}
