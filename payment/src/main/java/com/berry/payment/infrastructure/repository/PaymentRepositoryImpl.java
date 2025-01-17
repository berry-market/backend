package com.berry.payment.infrastructure.repository;

import com.berry.payment.application.dto.PaymentGetResDto;
import com.berry.payment.domain.model.Payment;
import com.berry.payment.domain.repository.PaymentRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

  private final PaymentJpaRepository jpaRepository;
  private final RedisPaymentRepository redisPaymentRepository;
  private final RedisIdempotencyRepository redisIdempotencyRepository;

  @Override
  public void saveTempPaymentData(String paymentId, int amount, Duration ttl) {
    redisPaymentRepository.saveTempPaymentData(paymentId, amount, ttl);
  }

  @Override
  public Map<Object, Object> getTempPaymentData(String paymentId) {
    return redisPaymentRepository.getTempPaymentData(paymentId);
  }

  @Override
  public void deleteTempPaymentData(String paymentId) {
    redisPaymentRepository.deleteTempPaymentData(paymentId);
  }

  @Override
  public void saveResponse(String idempotencyKey, Object response, Duration ttl) {
    redisIdempotencyRepository.saveResponse(idempotencyKey, response, ttl);
  };

  @Override
  public Object getResponse(String idempotencyKey) {
    return redisIdempotencyRepository.getResponse(idempotencyKey);
  };

  @Override
  public void save(Payment payment) {
    jpaRepository.save(payment);
  }

  @Override
  public Optional<Payment> findByPaymentKey(String paymentKey) {
    return jpaRepository.findByPaymentKey(paymentKey);
  }

  @Override
  public Page<PaymentGetResDto> findAllByBuyerIdAndRequestedAtBetween(
      Long buyerId,
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      Pageable pageable
  ) {
    return jpaRepository.findAllByBuyerIdAndRequestedAtBetween(
        buyerId,
        startDateTime,
        endDateTime,
        pageable
    );
  }
}