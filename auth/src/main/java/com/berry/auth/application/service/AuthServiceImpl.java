package com.berry.auth.application.service;

import com.berry.auth.domain.model.Role;
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
}
