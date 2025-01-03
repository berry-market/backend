package com.berry.auth.infrastructure.security.jwt;

import java.util.Collections;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private final Long userId;
  @Getter
  private final String nickname;
  @Getter
  private final String role;

  public JwtAuthenticationToken(Long userId, String nickname, String role, Object credentials) {
    super(Collections.emptyList());
    this.userId = userId;
    this.nickname = nickname;
    this.role = role;
    setAuthenticated(false);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return userId;
  }
}
