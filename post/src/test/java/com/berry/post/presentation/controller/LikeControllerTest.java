package com.berry.post.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.post.application.service.like.LikeServiceImpl;
import com.berry.post.presentation.request.like.CreatePostLikeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class LikeControllerTest {

    @InjectMocks
    private LikeController likeController;

    @Mock
    private LikeServiceImpl likeServiceImpl;

    @Captor
    private ArgumentCaptor<Long> userIdCaptor;

    @Captor
    private ArgumentCaptor<CreatePostLikeRequest> requestCaptor;

    @Test
    @DisplayName("찜 생성 성공 테스트")
    void createPostLike() {
        // given
        CreatePostLikeRequest request = new CreatePostLikeRequest(1L);
        Long userId = 1L;

        // when
        ApiResponse<Void> response = likeController.createPostLike(request, userId);

        // then
        then(likeServiceImpl).should().createPostLike(requestCaptor.capture(), userIdCaptor.capture());

        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(request, requestCaptor.getValue());
    }
}
