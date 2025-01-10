package com.berry.payment.application.service;

import com.berry.payment.application.dto.ConfirmPaymentReqDto;
import com.berry.payment.application.dto.PaymentGetResDto;
import com.berry.payment.application.dto.TempPaymentReqDto;
import com.berry.payment.application.dto.TossPaymentResDto;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

  void saveTempPaymentData(TempPaymentReqDto request);

  public TossPaymentResDto confirmPayment(ConfirmPaymentReqDto request);

  Page<PaymentGetResDto> getPayments (
      Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
