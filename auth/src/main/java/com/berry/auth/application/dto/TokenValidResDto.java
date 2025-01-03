package com.berry.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenValidResDto {
  private Long userId;
  private String nickname;
  private String role;
}