package com.berry.post.presentation.controller;

import com.berry.post.application.service.post.PostServiceImpl;
import com.berry.post.presentation.response.Post.PostDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/server/v1/posts")
public class PostServerController {

  private final PostServiceImpl postServiceImpl;

  @GetMapping("/{postId}")
  public PostDetailsResponse getPost(@PathVariable Long postId) {
    return postServiceImpl.getPost(postId);
  }
}
