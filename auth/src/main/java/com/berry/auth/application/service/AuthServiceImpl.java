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
import com.berry.common.response.ResErrorCode;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final JwtTokenParser jwtTokenParser;
  private final JwtTokenValidator jwtTokenValidator;
  private final JwtTokenFactory jwtTokenFactory;
  private final UserClient userClient;
  private final AuthRepository authRepository;

  @Override
  @Transactional
  public void logout(String authorizationHeader, String refreshToken,
      HttpServletResponse response) {

    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "인증정보가 없거나 유효하지 않습니다");
    }

    if (refreshToken == null) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "리프레시 토큰이 존재하지 않습니다");
    }

    String accessToken = authorizationHeader.substring(7);

    boolean tokenDeleted = authRepository.deleteRefreshToken(refreshToken);
    if (!tokenDeleted) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "리프레시 토큰 삭제에 실패하였습니다.");
    }

    long remainingExpiration =
        jwtTokenParser.getExpiration(accessToken) - System.currentTimeMillis();
    if (remainingExpiration <= 0) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "액세스토큰이 이미 만료되었습니다.");
    }

    boolean isBlacklisted = authRepository.addToBlacklist(accessToken, remainingExpiration);
    if (!isBlacklisted) {
      throw new CustomApiException(ResErrorCode.INTERNAL_SERVER_ERROR, "액세스토큰 블랙리스트 등록에 실패하였습니다.");
    }

  Cookie expiredCookie = new Cookie("refreshToken", null);
    expiredCookie.setHttpOnly(true);
    expiredCookie.setSecure(true);
    expiredCookie.setPath("/");
    expiredCookie.setMaxAge(0);
    response.addCookie(expiredCookie);
  }

  @Override
  @Transactional
  public String refreshAccessToken(String refreshToken) {

    jwtTokenValidator.validateRefreshToken(refreshToken);

    Long userId = jwtTokenParser.getUserId(refreshToken);

    UserInfoResDto user;

    try {
    user = userClient.getUserById(userId).getData();
    } catch (
  FeignException e) {
      throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
          "User Service: " + e.getMessage());
    }

    return jwtTokenFactory.createAccessToken(user.getUserId(), user.getNickname(),
        user.getRole());
  }

  @Override
  @Transactional
  public TokenValidResDto validateToken(String accessToken) {

    jwtTokenValidator.validateAccessToken(accessToken);

    Long userId = jwtTokenParser.getUserId(accessToken);
    String username = jwtTokenParser.getNickname(accessToken);
    String role = jwtTokenParser.getRole(accessToken);

    return new TokenValidResDto(userId, username, role);
  }
}
