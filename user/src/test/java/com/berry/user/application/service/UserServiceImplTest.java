package com.berry.user.application.service;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.role.Role;
import com.berry.user.domain.model.User;
import com.berry.user.domain.repository.UserJpaRepository;
import com.berry.user.presentation.dto.response.GetUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("유저 프로필 조회 성공 테스트")
    void getUserById_ShouldReturnUser_WhenRequestIsValid() {
        // given
        Long headerUserId = 1L;
        Long userId = 1L;
        String role = "MEMBER";

        User user = User.builder()
            .id(userId)
            .nickname("nickname")
            .email("email@example.com")
            .profileImage("profileImage")
            .point(0)
            .role(Role.MEMBER)
            .build();

        given(userJpaRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        GetUserResponse response = userService.getUserById(headerUserId, userId, role);

        // then
        assertEquals(user.getId(), response.userId());
        assertEquals(user.getNickname(), response.nickname());
        assertEquals(user.getEmail(), response.email());
    }

    @Test
    @DisplayName("유저 프로필 조회 실패 테스트 - 권한 없음")
    void getUserById_ShouldThrowException_WhenUserIdsDoNotMatch() {
        // given
        Long headerUserId = 1L;
        Long userId = 2L;
        String role = "MEMBER";

        // when & then
        assertThrows(CustomApiException.class, () -> userService.getUserById(headerUserId, userId, role));
    }

    @Test
    @DisplayName("유저 프로필 조회 실패 테스트 - 잘못된 역할")
    void getUserById_ShouldThrowException_WhenRoleIsInvalid() {
        // given
        Long headerUserId = 1L;
        Long userId = 1L;
        String role = "GUEST";

        // when & then
        assertThrows(CustomApiException.class, () -> userService.getUserById(headerUserId, userId, role));
    }
}
