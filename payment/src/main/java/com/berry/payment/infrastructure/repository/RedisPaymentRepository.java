package com.berry.payment.infrastructure.repository;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisPaymentRepository {

  private final RedisTemplate<String, Object> redisTemplate;

  public void saveTempPaymentData(String orderId, int amount, Duration ttl) {
    String key = getKey(orderId);
    Map<String, Object> paymentData = new HashMap<>();
    paymentData.put("amount", amount);

    redisTemplate.opsForHash().putAll(key, paymentData);

    redisTemplate.expire(key, ttl);
  }

  public Map<Object, Object> getTempPaymentData(String orderId) {
    String key = getKey(orderId);
    return redisTemplate.opsForHash().entries(key);
  }

  public void deleteTempPaymentData(String orderId) {
    String key = getKey(orderId);
    redisTemplate.delete(key);
  }

  private String getKey(String orderId) {
    return "tempPayment:" + orderId;
  }
}