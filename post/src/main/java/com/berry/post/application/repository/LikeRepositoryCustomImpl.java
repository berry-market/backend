package com.berry.post.application.repository;

import com.berry.post.domain.model.QLike;
import com.berry.post.domain.model.QPost;
import com.berry.post.presentation.response.like.LikeResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryCustomImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QLike like = QLike.like;
    QPost post = QPost.post;

    @Override
    public List<LikeResponse> findLikesByUserId(Long userId) {
        return queryFactory.select(Projections.constructor(
                LikeResponse.class,
                like.id.as("likeId"),
                post.id.as("postId"),
                post.productName,
                post.immediatePrice,
                post.productStatus,
                post.productImage,
                post.viewCount,
                like.createdAt
            ))
            .from(like)
            .join(post).on(like.postId.eq(post.id))
            .where(like.userId.eq(userId))
            .orderBy(post.createdAt.desc())
            .fetch();
    }
}
