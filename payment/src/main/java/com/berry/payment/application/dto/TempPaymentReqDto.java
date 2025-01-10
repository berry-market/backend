package com.berry.payment.application.dto;

import lombok.Getter;

@Getter
public class TempPaymentReqDto {
  private String orderId;
  private int amount;
}