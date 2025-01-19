package com.berry.auth.infrastructure.security.jwt;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenValidator {

  private final Key signingKey;

  public JwtTokenValidator(@Value("${jwt.secret}") String secretKey) {
    this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  public void validateAccessToken(String AccessToken) {
    Claims claims = extractClaims(AccessToken);
    if (!"access".equals(claims.get("type", String.class))) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Not a access token.");
    }
  }

  public void validateRefreshToken(String RefreshToken) {
    Claims claims = extractClaims(RefreshToken);
    if (!"refresh".equals(claims.get("type", String.class))) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Not a refresh token.");
    }
  }

  public Claims extractClaims(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(signingKey)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Expired Token");
    } catch (JwtException e) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Invalid Token");
    }
  }
}

