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

    redisTemplate.opsForValue().set(refreshKey, String.valueOf(userId), remainingExpiration, TimeUnit.MILLISECONDS);

    return redisTemplate.hasKey(refreshKey);
  }

  @Override
  public boolean deleteRefreshToken(String refreshToken) {
    String refreshKey = getKey("refreshToken", refreshToken);

    return Boolean.TRUE.equals(redisTemplate.delete(refreshKey));
  }

  public boolean addToBlacklist(String accessToken, long remainingExpiration) {
    String blacklistKey = "blacklist:" + accessToken;

    redisTemplate.opsForValue().set(
        blacklistKey,
        "blacklisted",
        remainingExpiration,
        TimeUnit.MILLISECONDS
    );

    return redisTemplate.hasKey(blacklistKey);
  }

  @Override
  public boolean isBlacklisted(String accessToken) {
    return redisTemplate.hasKey("blacklist:" + accessToken);
  }

  private String getKey(String type, String token) {
    return type + ":" + token;
  }
}
