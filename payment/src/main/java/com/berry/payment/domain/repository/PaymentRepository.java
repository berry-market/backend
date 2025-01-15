package com.berry.payment.domain.repository;

import com.berry.payment.application.dto.PaymentGetResDto;
import com.berry.payment.domain.model.Payment;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentRepository {

  void saveTempPaymentData(String paymentId, int amount, Duration ttl);

  Map<Object, Object> getTempPaymentData(String paymentId);

  void deleteTempPaymentData(String paymentId);

  void saveResponse(String idempotencyKey, Object response, Duration ttl);

  Object getResponse(String idempotencyKey);

  void save(Payment payment);

  Optional<Payment> findByPaymentKey(String paymentKey);

  Page<PaymentGetResDto> findAllByBuyerIdAndRequestedAtBetween(
      Long buyerId,
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      Pageable pageable
  );
}