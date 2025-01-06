package com.berry.auth.application.service;

import com.berry.auth.application.dto.TokenValidResDto;

public interface AuthService {

  String refreshAccessToken(String refreshToken);

  void logout(String accessToken);

  TokenValidResDto validateToken(String accessToken);
}
