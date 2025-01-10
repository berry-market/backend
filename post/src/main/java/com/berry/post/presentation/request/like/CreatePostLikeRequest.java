package com.berry.post.presentation.request.like;

import jakarta.validation.constraints.NotNull;

public record CreatePostLikeRequest(
    @NotNull
    Long postId
) {
}
