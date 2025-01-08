package com.berry.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResDto {
  private String accessToken;
  private Long userId;
  private String nickname;
  private String role;
}
