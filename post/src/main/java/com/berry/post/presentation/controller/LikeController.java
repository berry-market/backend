package com.berry.post.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.post.application.service.like.LikeService;
import com.berry.post.presentation.request.like.CreatePostLikeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createPostLike(
        @Valid @RequestBody CreatePostLikeRequest request,
        @RequestHeader("X-UserId") Long userId
    ) {
        likeService.createPostLike(request, userId);
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.CREATED, "찜이 생성되었습니다."));
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<ApiResponse<Void>> deletePostLike(
        @RequestHeader("X-UserId") Long headerUserId,
        @PathVariable("likeId") Long likeId
    ) {
        likeService.deletePostLike(headerUserId, likeId);
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.DELETED, "찜이 삭제되었습니다."));
    }

}
