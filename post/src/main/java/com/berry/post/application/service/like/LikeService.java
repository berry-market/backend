package com.berry.post.application.service.like;

import com.berry.post.presentation.request.like.CreatePostLikeRequest;
import com.berry.post.presentation.response.like.LikeResponse;

import java.util.List;

public interface LikeService {
    void createPostLike(CreatePostLikeRequest request, Long userId);

    void deletePostLike(Long headerUserId, Long likeId);

    List<LikeResponse> getLikes(Long headerUserId, String role);
}
