package com.berry.post.presentation.response.like;

import com.berry.post.domain.model.ProductStatus;

import java.time.LocalDateTime;

public record LikeResponse(
    Long likeId,
    Long postId,
    String productName,
    Integer immediatePrice,
    ProductStatus productStatus,
    String productImage,
    Integer viewCount,
    LocalDateTime createdAt
) {
}
