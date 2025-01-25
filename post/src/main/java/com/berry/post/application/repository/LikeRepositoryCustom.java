package com.berry.post.application.repository;

import com.berry.post.presentation.response.like.LikeResponse;

import java.util.List;

public interface LikeRepositoryCustom {

    List<LikeResponse> findLikesByUserId(Long userId);
}
