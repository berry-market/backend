package com.berry.payment.application.service;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.payment.application.dto.ConfirmPaymentReqDto;
import com.berry.payment.application.dto.PaymentGetResDto;
import com.berry.payment.application.dto.TempPaymentReqDto;
import com.berry.payment.application.dto.TossCancelReqDto;
import com.berry.payment.application.dto.TossCancelResDto;
import com.berry.payment.application.dto.TossPaymentResDto;
import com.berry.payment.domain.model.Payment;
import com.berry.payment.domain.repository.PaymentRepository;
import com.berry.payment.infrastructure.client.TossPaymentClient;
import com.berry.payment.application.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final TossPaymentClient tossPaymentClient;
  private final PaymentProducerService paymentProducer;

  @Override
  @Transactional
  public void saveTempPaymentData(TempPaymentReqDto request) {
    String orderId = request.getOrderId();
    int amount = request.getAmount();

    paymentRepository.saveTempPaymentData(orderId, amount, Duration.ofMinutes(10));
  }

  @Override
  @Transactional
  public TossPaymentResDto confirmPayment(ConfirmPaymentReqDto request) {
    String orderId = request.getOrderId();
    int amount = request.getAmount();
    String paymentKey = request.getPaymentKey();

    Payment existingPayment = paymentRepository.findByPaymentKey(paymentKey).orElse(null);
    if (existingPayment != null) {
      return TossPaymentResDto.fromEntity(existingPayment);
    }

    Map<Object, Object> tempData = paymentRepository.getTempPaymentData(orderId);

    if (tempData == null || !tempData.containsKey("amount")) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "유효하지 않은 결제 데이터입니다.");
    }

    int storedAmount = (int) tempData.get("amount");

    if (storedAmount != amount) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "결제 금액이 일치하지 않습니다.");
    }

    TossPaymentResDto response = tossPaymentClient.confirmPayment(orderId, paymentKey, amount);

    Payment payment = Payment.createFrom(response, request.getBuyerId());
    paymentRepository.save(payment);

    paymentRepository.deleteTempPaymentData(orderId);

    PaymentEvent event = new PaymentEvent(request.getBuyerId(),
        response.getTotalAmount());
    paymentProducer.sendPaymentEvent(event);

    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PaymentGetResDto> getPayments(
      Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
    return paymentRepository.findAllByBuyerIdAndRequestedAtBetween(
        userId,
        startDate.atStartOfDay(),
        endDate.plusDays(1).atStartOfDay(),
        pageable
    );
  }

  @Override
  @Transactional
  public void cancelPayment(Long userId, String paymentKey, TossCancelReqDto request,
      String idempotencyKey) {

    Object cachedResponse = paymentRepository.getResponse(idempotencyKey);
    if (cachedResponse != null) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "이미 처리된 취소 요청입니다.");
    }

    Payment payment = paymentRepository.findByPaymentKey(paymentKey)
        .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "결제 내역을 찾을 수 없습니다."));

    int newCancelAmount =
        request.getCancelAmount() != null ? request.getCancelAmount()
            : payment.getBalanceAmount();

    if (newCancelAmount > payment.getBalanceAmount()) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, "취소 금액이 결제 금액을 초과할 수 없습니다.");
    }

    TossCancelResDto response = tossPaymentClient.cancelPayment(
        paymentKey, request.getCancelReason(), request.getCancelAmount(),
        idempotencyKey);

    paymentRepository.saveResponse(idempotencyKey, response, Duration.ofMinutes(5));

    try {
      payment.updateCancelInfo(
          response.status(),
          response.balanceAmount(),
          response.transactionKey()
      );
    } catch (Exception e) {
      log.error("결제 취소 정보 업데이트 중 오류 발생: {}", e.getMessage());
      try {
        JSONObject paymentInfo = tossPaymentClient.getPaymentInfo(paymentKey);
        TossCancelResDto latestResponse = TossCancelResDto.fromJson(paymentInfo);

        // TODO: 다른 방법 생각해보기
        payment.updateCancelInfo(
            latestResponse.status(),
            latestResponse.balanceAmount(),
            latestResponse.transactionKey()
        );

        log.info("결제 취소 정보 업데이트 재시도 성공 - paymentKey: {}", paymentKey);
      } catch (Exception ex) {
        log.error("결제 취소 정보 업데이트 재시도 실패: {}", ex.getMessage());
        throw new CustomApiException(ResErrorCode.API_CALL_FAILED, "결제 취소 정보 업데이트 중 오류가 발생했습니다.");
      }
    }

    PaymentEvent event = new PaymentEvent(userId,
        request.getCancelAmount());
    paymentProducer.sendCancelEvent(event);
  }
}