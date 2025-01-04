package com.berry.auth.infrastructure.repository;

import com.berry.auth.domain.repository.AuthRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisTokenRepository implements AuthRepository {

  private final RedisTemplate<String, String> redisTemplate;

  @Value("${jwt.refresh.expiration}")
  private int refreshTokenValidity;

  @Override
  public boolean saveRefreshToken(Long userId, String refreshToken) {
    long remainingExpiration = refreshTokenValidity * 1000L;

    if (remainingExpiration <= 0) {
      throw new IllegalArgumentException("남은시간은 0 이상이어야합니다.");
    }

    String refreshKey = getKey("refreshToken", refreshToken);
    String userKey = getKey("userId", userId.toString());

    redisTemplate.opsForValue().set(refreshKey, userId.toString(), remainingExpiration, TimeUnit.MILLISECONDS);
    redisTemplate.opsForValue().set(userKey, refreshToken, remainingExpiration, TimeUnit.MILLISECONDS);

    return redisTemplate.hasKey(refreshKey) && redisTemplate.hasKey(userKey);
  }

  // 키 구조
  private String getKey(String type, String token) {
    return type + ":" + token;
  }
}
