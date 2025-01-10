package com.berry.post.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.post.application.service.like.LikeService;
import com.berry.post.presentation.request.like.CreatePostLikeRequest;
import com.berry.post.presentation.response.like.LikeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ApiResponse<Void> createPostLike(
        @Valid @RequestBody CreatePostLikeRequest request,
        @RequestHeader("X-UserId") Long userId
    ) {
        likeService.createPostLike(request, userId);
        return ApiResponse.OK(ResSuccessCode.CREATED, "찜이 생성되었습니다.");
    }

    @GetMapping
    public ApiResponse<List<LikeResponse>> getLikes(
        @RequestHeader("X-UserId") Long headerUserId,
        @RequestHeader("X-Role") String role
    ) {
        return ApiResponse.OK(ResSuccessCode.READ, likeService.getLikes(headerUserId, role));
    }

    @DeleteMapping("/{likeId}")
    public ApiResponse<Void> deletePostLike(
        @RequestHeader("X-UserId") Long headerUserId,
        @PathVariable("likeId") Long likeId
    ) {
        likeService.deletePostLike(headerUserId, likeId);
        return ApiResponse.OK(ResSuccessCode.DELETED, "찜이 삭제되었습니다.");
    }

}
