package com.berry.payment.infrastructure.repository;

import com.berry.payment.domain.repository.PaymentRepository;
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

  // 결제 임시 데이터 저장
  public void saveTempPaymentData(String orderId, int amount, Duration ttl) {
    String key = getKey(orderId);
    Map<String, Object> paymentData = new HashMap<>();
    paymentData.put("amount", amount);

    // Redis에 데이터 저장
    redisTemplate.opsForHash().putAll(key, paymentData);

    // TTL 설정
    redisTemplate.expire(key, ttl);
  }

  // 결제 임시 데이터 조회
  public Map<Object, Object> getTempPaymentData(String orderId) {
    String key = getKey(orderId);
    return redisTemplate.opsForHash().entries(key);
  }

  // 결제 임시 데이터 삭제
  public void deleteTempPaymentData(String orderId) {
    String key = getKey(orderId);
    redisTemplate.delete(key);
  }

  // 키 구조
  private String getKey(String orderId) {
    return "tempPayment:" + orderId;
  }
}