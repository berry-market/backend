package com.berry.post.application.service.like;

import com.berry.post.domain.model.Like;
import com.berry.post.domain.repository.LikeRepository;
import com.berry.post.presentation.request.like.CreatePostLikeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public void createPostLike(CreatePostLikeRequest request, Long userId) {
        Like like = Like.builder()
            .userId(userId)
            .postId(request.postId())
            .build();
        likeRepository.save(like);
    }
}
