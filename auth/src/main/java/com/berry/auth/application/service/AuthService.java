package com.berry.auth.application.service;

import com.berry.auth.application.dto.TokenValidResDto;

public interface AuthService {

  String refreshAccessToken(String refreshToken);

  TokenValidResDto validateToken(String accessToken);
}
