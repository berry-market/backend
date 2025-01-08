package com.berry.auth.application.dto;

import lombok.Getter;

@Getter
public class LoginReqDto {
  private String nickname;
  private String password;
}
