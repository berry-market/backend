package com.berry.payment.application.service;

import com.berry.payment.application.dto.TempPaymentReqDto;
import com.berry.payment.domain.repository.PaymentRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;

  @Override
  public void saveTempPaymentData(TempPaymentReqDto request) {
    String orderId = request.getOrderId();
    int amount = request.getAmount();

    // Redis에 임시 결제 데이터 저장 (TTL: 10분)
    paymentRepository.saveTempPaymentData(orderId, amount, Duration.ofMinutes(10));
  }
  
}