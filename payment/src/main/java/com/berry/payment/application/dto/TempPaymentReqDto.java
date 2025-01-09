package com.berry.payment.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TempPaymentReqDto {
  private String orderId;
  private int amount;
}