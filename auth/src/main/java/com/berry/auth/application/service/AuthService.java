package com.berry.auth.application.service;

import com.berry.auth.application.dto.TokenValidResDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

  String refreshAccessToken(String refreshToken);

  void logout(String authorizationHeader, String refreshToken,
      HttpServletResponse response);

  TokenValidResDto validateToken(String accessToken);
}
