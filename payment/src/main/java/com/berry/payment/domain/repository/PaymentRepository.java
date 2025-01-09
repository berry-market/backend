package com.berry.payment.domain.repository;

import com.berry.payment.domain.model.Payment;
import java.time.Duration;
import java.util.Map;

public interface PaymentRepository {

  void saveTempPaymentData(String paymentId, int amount, Duration ttl);

  Map<Object, Object> getTempPaymentData(String paymentId);

  void deleteTempPaymentData(String paymentId);

  Payment save(Payment payment);

}