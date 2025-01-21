package com.berry.user.presentation.controller;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResErrorCode;
import com.berry.common.role.Role;
import com.berry.user.application.service.UserServiceImpl;
import com.berry.user.presentation.dto.response.GetUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<Long> userIdCaptor;

    @Captor
    private ArgumentCaptor<Long> headerUserIdCaptor;

    @Captor
    private ArgumentCaptor<String> roleCaptor;

    @Test
    @DisplayName("유저 프로필 조회 성공 테스트")
    void getUserById_Success() {
        // given
        Long headerUserId = 1L;
        Long userId = 1L;
        String role = "MEMBER";

        GetUserResponse expectedResponse = new GetUserResponse(
            userId, "nickname", "email@example.com", "profileImage", 0, Role.MEMBER);

        given(userService.getUserById(headerUserId, userId, role)).willReturn(expectedResponse);

        // when
        ApiResponse<GetUserResponse> response = userController.getUserById(headerUserId, role, userId);

        // then
        assertEquals(expectedResponse, response.getData());
        then(userService).should().getUserById(headerUserIdCaptor.capture(), userIdCaptor.capture(), roleCaptor.capture());

        assertEquals(headerUserId, headerUserIdCaptor.getValue());
        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(role, roleCaptor.getValue());
    }

    @Test
    @DisplayName("유저 프로필 조회 실패 테스트 - 권한 부족")
    void getUserById_Failure_Forbidden() {
        // given
        Long headerUserId = 1L;
        Long userId = 2L; // 다른 사용자 ID
        String role = "MEMBER";

        given(userService.getUserById(headerUserId, userId, role))
            .willThrow(new CustomApiException(ResErrorCode.FORBIDDEN, "권한이 없습니다."));

        // when & then
        CustomApiException exception = assertThrows(CustomApiException.class,
            () -> userController.getUserById(headerUserId, role, userId));

        assertEquals(ResErrorCode.FORBIDDEN, exception.getErrorCode());
        assertEquals("권한이 없습니다.", exception.getMessage());
    }
}