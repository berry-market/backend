package com.berry.gateway.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisTokenRepository {

  private final RedisTemplate<String, String> redisTemplate;

  public boolean isRefreshTokenValid(String refreshToken) {
    String refreshKey = getKey("refreshToken", refreshToken);
    return redisTemplate.hasKey(refreshKey);
  }

  public boolean isBlacklisted(String accessToken) {
    return redisTemplate.hasKey("blacklist:" + accessToken);
  }

  private String getKey(String type, String value) {
    return type + ":" + value;
  }
}
