package com.berry.post.application.service.like;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.domain.model.Like;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.repository.LikeRepository;
import com.berry.post.domain.repository.PostRepository;
import com.berry.post.presentation.request.like.CreatePostLikeRequest;
import com.berry.post.presentation.response.like.LikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    @CacheEvict(cacheNames = "posts", allEntries = true)
    public void createPostLike(CreatePostLikeRequest request, Long userId) {
        getPostById(request.postId());

        Like like = Like.builder()
            .userId(userId)
            .postId(request.postId())
            .createdAt(LocalDateTime.now())
            .build();

        Post post = postRepository.findByIdAndDeletedYNFalse(request.postId()).orElseThrow(
            () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
        );
        post.plusLikeCount();

        likeRepository.save(like);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "posts", allEntries = true)
    public void deletePostLike(Long headerUserId, Long postId) {
        getPostById(postId);
        Like like = likeRepository.findByUserIdAndPostId(headerUserId, postId)
            .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "찜을 찾을 수 없습니다."));
        if (!Objects.equals(like.getUserId(), headerUserId)) {
            throw new CustomApiException(ResErrorCode.FORBIDDEN, "접근 권한이 없습니다.");
        }

        Post post = postRepository.findByIdAndDeletedYNFalse(postId).orElseThrow(
            () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
        );
        post.minusLikeCount();

        likeRepository.delete(like);
    }

    @Override
    public List<LikeResponse> getLikes(Long headerUserId, String role) {
        if ("ADMIN".equals(role)) {
            throw new CustomApiException(ResErrorCode.FORBIDDEN, "접근 권한이 없습니다.");
        }
        List<Like> likeList = likeRepository.findByUserId(headerUserId);
        return likeList.stream()
            .map(like -> new LikeResponse(
                like.getId(),
                like.getPostId(),
                like.getCreatedAt()
            ))
            .toList();
    }

    private void getPostById(Long postId) {
        postRepository.findById(postId)
            .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "게시글을 찾을 수 없습니다."));
    }
}
