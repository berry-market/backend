package com.berry.auth.infrastructure.security.jwt;

import com.berry.auth.domain.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenFactory {

  private final Key signingKey;
  @Value("${jwt.access.expiration}")
  private int accessTokenValidity;
  @Value("${jwt.refresh.expiration}")
  private int refreshTokenValidity;


  public JwtTokenFactory(@Value("${jwt.secret}") String secretKey) {
    this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  // 액세스 토큰 생성
  public String createAccessToken(Long userId, String nickname, Role role) {
    return createToken(userId, nickname, role, accessTokenValidity * 1000L, "access"); // 초 → 밀리초
  }

  // 리프레시 토큰 생성
  public String createRefreshToken(Long userId) {
    return createToken(userId, null, null, refreshTokenValidity * 1000L, "refresh");
  }

  private String createToken(Long userId, String nickname, Role role, long validity, String tokenType) {
    Claims claims = Jwts.claims().setSubject(userId.toString());
    claims.put("type", tokenType);

    if (nickname != null) claims.put("nickname", nickname);
    if (role != null) claims.put("role", role.toString());

    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + validity))
        .signWith(signingKey)
        .compact();
  }
}

