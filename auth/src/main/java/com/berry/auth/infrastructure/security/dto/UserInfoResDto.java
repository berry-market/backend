package com.berry.auth.infrastructure.security.dto;

import com.berry.auth.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResDto {
  private Long userId;
  private String nickname;
  private String profileImage;
  private Role role;
}
