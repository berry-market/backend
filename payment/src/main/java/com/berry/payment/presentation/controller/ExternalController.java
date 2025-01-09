package com.berry.payment.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.payment.application.dto.TempPaymentReqDto;
import com.berry.payment.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class ExternalController {

  private final PaymentService paymentService;

  @PostMapping
  public ApiResponse<Void> saveTempPaymentData(@RequestBody TempPaymentReqDto request) {
    // 서비스 호출
    paymentService.saveTempPaymentData(request);

    return ApiResponse.OK(ResSuccessCode.SUCCESS, "결제 정보를 임시 저장하였습니다");
  }
}
