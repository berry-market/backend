package com.berry.post.application.service.like;

import com.berry.post.presentation.request.like.CreatePostLikeRequest;

public interface LikeService {
    void createPostLike(CreatePostLikeRequest request, Long userId);
}
