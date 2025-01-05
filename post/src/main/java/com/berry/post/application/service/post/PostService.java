package com.berry.post.application.service.post;

import com.berry.post.presentation.request.Post.PostCreateRequest;
import com.berry.post.presentation.response.Post.PostResponse;
import com.querydsl.core.types.Predicate;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

  void createPost(PostCreateRequest postCreateRequest,
      MultipartFile multipartFile, List<MultipartFile> multipartFileList) throws IOException;

  Page<PostResponse> getPosts(String keyword, String type, Long postCategoryId, String sort, Pageable pageable);
}
