package com.berry.auth.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenParser {

  private final JwtTokenValidator validator;

  public JwtTokenParser(JwtTokenValidator validator) {
    this.validator = validator;
  }

  public Long getUserId(String token) {
    Claims claims = validator.extractClaims(token);
    return Long.parseLong(claims.getSubject());
  }

  public String getNickname(String token) {
    return validator.extractClaims(token).get("nickname", String.class);
  }

  public String getRole(String token) {
    return validator.extractClaims(token).get("role", String.class);
  }

  public long getExpiration(String token) {
    return validator.extractClaims(token).getExpiration().getTime();
  }
}

