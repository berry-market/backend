package com.berry.payment.infrastructure.repository;

import com.berry.payment.domain.model.Payment;
import com.berry.payment.domain.repository.PaymentRepository;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

  private final PaymentJpaRepository jpaRepository;
  private final RedisPaymentRepository redisRepository;

  @Override
  public void saveTempPaymentData(String paymentId, int amount, Duration ttl) {
    redisRepository.saveTempPaymentData(paymentId, amount, ttl);
  }

  @Override
  public Map<Object, Object> getTempPaymentData(String paymentId) {
    return redisRepository.getTempPaymentData(paymentId);
  }

  @Override
  public void deleteTempPaymentData(String paymentId) {
    redisRepository.deleteTempPaymentData(paymentId);
  }

  @Override
  public Payment save(Payment payment) {
    return jpaRepository.save(payment);
  }
}