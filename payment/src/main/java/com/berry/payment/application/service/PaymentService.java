package com.berry.payment.application.service;

import com.berry.payment.application.dto.ConfirmPaymentReqDto;
import com.berry.payment.application.dto.TempPaymentReqDto;
import com.berry.payment.application.dto.TossPaymentResDto;

public interface PaymentService {

  void saveTempPaymentData(TempPaymentReqDto request);

  public TossPaymentResDto confirmPayment(ConfirmPaymentReqDto request);
}
