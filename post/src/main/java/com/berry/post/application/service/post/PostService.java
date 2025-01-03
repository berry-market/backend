package com.berry.post.application.service.post;

import com.berry.post.presentation.request.Post.PostCreateRequest;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

  void createPost(PostCreateRequest postCreateRequest,
      MultipartFile multipartFile, List<MultipartFile> multipartFileList) throws IOException;
}
