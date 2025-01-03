package com.berry.auth.infrastructure.security.dto;

import com.berry.auth.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResDto {
  private Long id;
  private String nickname;
  private Role role;
  private String password;
}
