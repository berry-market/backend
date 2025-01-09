package com.berry.payment.domain.repository;

import java.time.Duration;
import java.util.Map;

public interface PaymentRepository {

  void saveTempPaymentData(String paymentId, int amount, Duration ttl);

  Map<Object, Object> getTempPaymentData(String paymentId);

  void deleteTempPaymentData(String paymentId);

}