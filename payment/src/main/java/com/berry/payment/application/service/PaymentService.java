package com.berry.payment.application.service;

import com.berry.payment.application.dto.TempPaymentReqDto;

public interface PaymentService {

  void saveTempPaymentData(TempPaymentReqDto request);

}
