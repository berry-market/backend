package com.berry.payment.application.service;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.payment.application.dto.ConfirmPaymentReqDto;
import com.berry.payment.application.dto.TempPaymentReqDto;
import com.berry.payment.application.dto.TossPaymentResDto;
import com.berry.payment.domain.model.Payment;
import com.berry.payment.domain.repository.PaymentRepository;
import com.berry.payment.infrastructure.client.TossPaymentClient;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final TossPaymentClient tossPaymentClient;

  @Override
  @Transactional
  public void saveTempPaymentData(TempPaymentReqDto request) {
    String orderId = request.getOrderId();
    int amount = request.getAmount();

    // Redis에 임시 결제 데이터 저장 (TTL: 10분)
    paymentRepository.saveTempPaymentData(orderId, amount, Duration.ofMinutes(10));
  }

  @Override
  @Transactional
  public TossPaymentResDto confirmPayment(ConfirmPaymentReqDto request) {
    String orderId = request.getOrderId();
    int amount = request.getAmount();
    String paymentKey = request.getPaymentKey();

    // Redis에서 임시 데이터 조회
    Map<Object, Object> tempData = paymentRepository.getTempPaymentData(orderId);

    if (tempData == null || !tempData.containsKey("amount")) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "유효하지 않은 결제 데이터입니다.");
    }

    int storedAmount = (int) tempData.get("amount");

    // 결제 금액 검증
    if (storedAmount != amount) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "결제 금액이 일치하지 않습니다.");
    }

    // 토스 API 호출
    TossPaymentResDto response = tossPaymentClient.confirmPayment(orderId, paymentKey, amount);

    // Redis 데이터 삭제
    paymentRepository.deleteTempPaymentData(orderId);

    // 결제 승인 성공: Payment 데이터 저장
    Payment payment = Payment.builder()
        .buyerId(request.getBuyerId())
        .paymentKey(response.getPaymentKey())
        .orderId(response.getOrderId())
        .orderName(response.getOrderName())
        .amount(response.getTotalAmount())
        .paymentMethod(response.getMethod())
        .paymentStatus(response.getStatus())
        .requestedAt(
            LocalDateTime.parse(response.getRequestedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        .approvedAt(response.getApprovedAt() != null
            ? LocalDateTime.parse(response.getApprovedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            : null)
        .build();

    paymentRepository.save(payment);

    return response;
  }
}