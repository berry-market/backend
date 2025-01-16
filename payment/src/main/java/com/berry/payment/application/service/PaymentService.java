package com.berry.payment.application.service;

import com.berry.payment.application.dto.ConfirmPaymentReqDto;
import com.berry.payment.application.dto.PaymentGetResDto;
import com.berry.payment.application.dto.TempPaymentReqDto;
import com.berry.payment.application.dto.TossCancelReqDto;
import com.berry.payment.application.dto.TossPaymentResDto;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentService {

  void saveTempPaymentData(TempPaymentReqDto request);

  TossPaymentResDto confirmPayment(Long userId, ConfirmPaymentReqDto request);

  Page<PaymentGetResDto> getPayments(
      Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable);

  void cancelPayment(Long userId, String paymentKey, TossCancelReqDto cancelRequest,
      String idempotencyKey);
}
