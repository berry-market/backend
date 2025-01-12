package com.berry.post.application.service.like;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.domain.model.Like;
import com.berry.post.domain.repository.LikeRepository;
import com.berry.post.presentation.request.like.CreatePostLikeRequest;
import com.berry.post.presentation.response.like.LikeResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
            .createdAt(LocalDateTime.now())
            .build();
        likeRepository.save(like);
    }

    @Override
    @Transactional
    public void deletePostLike(Long headerUserId, Long likeId) {
        Like like = likeRepository.findById(likeId)
            .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "찜을 찾을 수 없습니다."));
        if (!Objects.equals(like.getUserId(), headerUserId)) {
            throw new CustomApiException(ResErrorCode.FORBIDDEN, "접근 권한이 없습니다.");
        }
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
}
