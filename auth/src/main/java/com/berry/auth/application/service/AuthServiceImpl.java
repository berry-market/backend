package com.berry.auth.application.service;

import com.berry.auth.application.dto.TokenValidResDto;
import com.berry.auth.domain.model.Role;
import com.berry.auth.domain.repository.AuthRepository;
import com.berry.auth.infrastructure.client.UserClient;
import com.berry.auth.infrastructure.security.dto.UserResDto;
import com.berry.auth.infrastructure.security.jwt.JwtTokenFactory;
import com.berry.auth.infrastructure.security.jwt.JwtTokenParser;
import com.berry.auth.infrastructure.security.jwt.JwtTokenValidator;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import feign.FeignException;
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
  public void logout(String accessToken) {
    Long userId = jwtTokenParser.getUserId(accessToken);

    // 리프레시 토큰 삭제
    boolean tokenDeleted = authRepository.deleteRefreshToken(userId);
    if (!tokenDeleted) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "리프레시토큰 삭제를 실패하였습니다.");
    }

    // 액세스 토큰 남은시간 계산
    long remainingExpiration = jwtTokenParser.getExpiration(accessToken) - System.currentTimeMillis();
    if (remainingExpiration <= 0) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "액세스토큰이 이미 만료되었습니다.");
    }

    // 액세스 토큰 블랙리스트 추가
    boolean isBlacklisted = authRepository.addToBlacklist(accessToken, remainingExpiration);
    if (!isBlacklisted) {
      throw new CustomApiException(ResErrorCode.INTERNAL_SERVER_ERROR, "액세스토큰 블랙리스트 등록에 실패하였습니다.");
    }
  }

  @Override
  @Transactional
  public String refreshAccessToken(String refreshToken) {
    // 리프레시 토큰 유효성 검증
    jwtTokenValidator.validateRefreshToken(refreshToken);

    Long userId = jwtTokenParser.getUserId(refreshToken);

    UserResDto userResDto;

    // TODO: User Service 연동
//    try {
//    // 유저서비스에서 유저 정보 가져오기
//    user = userClient.getUserById(userId).getData();
//    } catch (
//  FeignException e) {
//      throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
//          "User Service: " + e.getMessage());
//    }

    userResDto = new UserResDto(1L, "user1", Role.MEMBER,
        "$2a$10$wjcbslxcYk5s4hkHLIdtZOnwfmxDCnH4Y82hRlcLLG9Qq0uCw02aW");

    return jwtTokenFactory.createAccessToken(userResDto.getId(), userResDto.getNickname(),
        userResDto.getRole());
  }

  @Override
  @Transactional
  public TokenValidResDto validateToken(String accessToken) {
    // 액세스 토큰 유효성 검증
    jwtTokenValidator.validateAccessToken(accessToken);

    // 검증된 유저정보 반환
    Long userId = jwtTokenParser.getUserId(accessToken);
    String username = jwtTokenParser.getNickname(accessToken);
    String role = jwtTokenParser.getRole(accessToken);

    return new TokenValidResDto(userId, username, role);
  }
}
