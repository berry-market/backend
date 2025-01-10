package com.berry.payment.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.payment.application.dto.ConfirmPaymentReqDto;
import com.berry.payment.application.dto.PaymentGetResDto;
import com.berry.payment.application.dto.TempPaymentReqDto;
import com.berry.payment.application.dto.TossPaymentResDto;
import com.berry.payment.application.service.PaymentService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class ExternalController {

  private final PaymentService paymentService;

  @PostMapping
  public ApiResponse<Void> saveTempPaymentData(@RequestBody TempPaymentReqDto request) {

    paymentService.saveTempPaymentData(request);

    return ApiResponse.OK(ResSuccessCode.SUCCESS, "결제 정보를 임시 저장하였습니다");
  }

  @PostMapping("/confirm")
  public synchronized ApiResponse<TossPaymentResDto> confirmPayment(
      @RequestBody ConfirmPaymentReqDto request) {
    TossPaymentResDto response = paymentService.confirmPayment(request);
    return ApiResponse.OK(ResSuccessCode.SUCCESS, response);
  }

  @GetMapping
  public ApiResponse<Page<PaymentGetResDto>> getPayments(
      @RequestParam Long userId,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
      @PageableDefault(size = 10) Pageable pageable) {
    Page<PaymentGetResDto> payments = paymentService.getPayments (
        userId, startDate, endDate, pageable);
    return ApiResponse.OK(ResSuccessCode.SUCCESS, payments);
  }
}
