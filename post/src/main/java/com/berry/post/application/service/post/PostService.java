package com.berry.post.application.service.post;

import com.berry.post.presentation.request.Post.PostCreateRequest;

public interface PostService {

  void createPost(PostCreateRequest postCreateRequest);

}
