package com.berry.payment.application.dto;

import lombok.Getter;

@Getter
public class ConfirmPaymentReqDto {
  private Long buyerId;
  private String orderId;
  private int amount;
  private String paymentKey;
}