package com.berry.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResDto {
  private final String accessToken;
  private final String refreshToken;
}
