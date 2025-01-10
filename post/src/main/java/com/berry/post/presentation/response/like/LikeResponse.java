package com.berry.post.presentation.response.like;

import java.time.LocalDateTime;

public record LikeResponse(
    Long likeId,
    Long postId,
    LocalDateTime createdAt
) {
}
