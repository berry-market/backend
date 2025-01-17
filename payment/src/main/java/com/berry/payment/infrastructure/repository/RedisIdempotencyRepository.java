package com.berry.payment.infrastructure.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisIdempotencyRepository {

  private final RedisTemplate<String, Object> redisTemplate;

  public void saveResponse(String idempotencyKey, Object response, Duration ttl) {
    String key = getKey(idempotencyKey);
    redisTemplate.opsForValue().set(key, response, ttl);
  }

  public Object getResponse(String idempotencyKey) {
    String key = getKey(idempotencyKey);
    return redisTemplate.opsForValue().get(key);
  }

  private String getKey(String idempotencyKey) {
    return "idempotency:" + idempotencyKey;
  }
}

