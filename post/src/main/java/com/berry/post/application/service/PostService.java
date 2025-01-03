package com.berry.post.application.service;

import com.berry.post.presentation.request.Post.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

  @Transactional
  public void createPost(PostCreateRequest postCreateRequest) {
  }
}
