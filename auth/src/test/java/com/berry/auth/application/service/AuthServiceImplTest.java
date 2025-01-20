package com.berry.auth.application.service;

import com.berry.auth.application.dto.TokenValidResDto;
import com.berry.auth.domain.model.Role;
import com.berry.auth.domain.repository.AuthRepository;
import com.berry.auth.infrastructure.client.UserClient;
import com.berry.auth.infrastructure.security.dto.UserInfoResDto;
import com.berry.auth.infrastructure.security.jwt.JwtTokenFactory;
import com.berry.auth.infrastructure.security.jwt.JwtTokenParser;
import com.berry.auth.infrastructure.security.jwt.JwtTokenValidator;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResErrorCode;
import com.berry.common.response.ResSuccessCode;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AuthServiceImplTest {

  @InjectMocks
  private AuthServiceImpl authService;

  @Mock
  private JwtTokenParser jwtTokenParser;

  @Mock
  private JwtTokenValidator jwtTokenValidator;

  @Mock
  private JwtTokenFactory jwtTokenFactory;

  @Mock
  private UserClient userClient;

  @Mock
  private AuthRepository authRepository;

  @Mock
  private HttpServletResponse response;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    doNothing().when(jwtTokenValidator).validateRefreshToken(anyString());
    doNothing().when(jwtTokenValidator).validateAccessToken(anyString());
    doNothing().when(response).addCookie(any(Cookie.class));
  }

  @Test
  void logout_ShouldInvalidateTokens_WhenValid() {
    // Given
    String authorizationHeader = "Bearer accessToken123";
    String refreshToken = "refreshToken123";

    when(jwtTokenParser.getExpiration("accessToken123")).thenReturn(System.currentTimeMillis() + 60000L);
    when(authRepository.deleteRefreshToken(refreshToken)).thenReturn(true);
    when(authRepository.addToBlacklist("accessToken123", 60000L)).thenReturn(true);

    // When
    authService.logout(authorizationHeader, refreshToken, response);

    // Then
    verify(authRepository, times(1)).deleteRefreshToken(refreshToken);
    verify(authRepository, times(1)).addToBlacklist("accessToken123", 60000L);
    verify(response, times(1)).addCookie(any(Cookie.class));
  }

  @Test
  void logout_ShouldThrowException_WhenAuthorizationHeaderIsInvalid() {
    // Given
    String invalidAuthorizationHeader = "InvalidToken";
    String refreshToken = "refreshToken123";

    // When / Then
    CustomApiException exception = assertThrows(CustomApiException.class, () ->
        authService.logout(invalidAuthorizationHeader, refreshToken, response)
    );

    assertEquals(ResErrorCode.UNAUTHORIZED, exception.getErrorCode());
  }

  @Test
  void refreshAccessToken_ShouldReturnNewAccessToken_WhenValid() {
    // Given
    String refreshToken = "validRefreshToken";
    Long userId = 1L;

    UserInfoResDto userInfo = new UserInfoResDto(userId, "testUser", null, Role.MEMBER);

    when(jwtTokenParser.getUserId(refreshToken)).thenReturn(userId);
    when(userClient.getUserById(userId))
        .thenReturn(ApiResponse.OK(ResSuccessCode.SUCCESS, userInfo));
    when(jwtTokenFactory.createAccessToken(userId, "testUser", Role.MEMBER))
        .thenReturn("newAccessToken");

    // When
    String newAccessToken = authService.refreshAccessToken(refreshToken);

    // Then
    assertEquals("newAccessToken", newAccessToken);
    verify(jwtTokenValidator, times(1)).validateRefreshToken(refreshToken);
    verify(userClient, times(1)).getUserById(userId);
  }

  @Test
  void refreshAccessToken_ShouldThrowException_WhenFeignExceptionOccurs() {
    // Given
    String refreshToken = "validRefreshToken";
    Long userId = 1L;

    when(jwtTokenParser.getUserId(refreshToken)).thenReturn(userId);
    when(userClient.getUserById(userId)).thenThrow(FeignException.class);

    // When / Then
    CustomApiException exception = assertThrows(CustomApiException.class, () ->
        authService.refreshAccessToken(refreshToken)
    );

    assertEquals(ResErrorCode.API_CALL_FAILED, exception.getErrorCode());
  }

  @Test
  void validateToken_ShouldReturnTokenValidResDto_WhenValid() {
    // Given
    String accessToken = "validAccessToken";
    Long userId = 1L;
    String username = "testUser";
    Role role = Role.MEMBER;

    when(jwtTokenParser.getUserId(accessToken)).thenReturn(userId);
    when(jwtTokenParser.getNickname(accessToken)).thenReturn(username);
    when(jwtTokenParser.getRole(accessToken)).thenReturn(role.name());

    // When
    TokenValidResDto result = authService.validateToken(accessToken);

    // Then
    assertNotNull(result);
    assertEquals(userId, result.getUserId());
    assertEquals(username, result.getNickname());
    assertEquals(role.name(), result.getRole());
  }

  @Test
  void validateToken_ShouldThrowException_WhenTokenIsInvalid() {
    // Given
    String invalidAccessToken = "invalidToken";

    doThrow(new CustomApiException(ResErrorCode.UNAUTHORIZED, "토큰이 유효하지 않습니다."))
        .when(jwtTokenValidator).validateAccessToken(invalidAccessToken);

    // When / Then
    CustomApiException exception = assertThrows(CustomApiException.class, () ->
        authService.validateToken(invalidAccessToken)
    );

    assertEquals(ResErrorCode.UNAUTHORIZED, exception.getErrorCode());
  }
}
