package com.berry.post.application.service.post;

import com.berry.post.presentation.request.Post.PostCreateRequest;
import com.berry.post.presentation.request.Post.PostUpdateRequest;
import com.berry.post.presentation.response.Post.PostDetailsResponse;
import com.berry.post.presentation.response.Post.PostListResponse;
import com.berry.post.presentation.response.Post.PostServerResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

  void createPost(PostCreateRequest postCreateRequest,
      MultipartFile productImage, List<MultipartFile> productDetailsImages, Long userId, String role) throws IOException;

  Page<PostListResponse> getPosts(String keyword, String type, Long postCategoryId,
      String sort, Pageable pageable, Long userId);

  PostDetailsResponse getPost(Long postId, Long userId);

  PostServerResponse getServerPost(Long postId);

  void updatePost(Long postId, PostUpdateRequest postUpdateRequest,
      MultipartFile productImage, List<MultipartFile> productDetailsImages, Long userId,
      String role)
      throws IOException;

  void deletePost(Long postId, Long userId, String role);
}
