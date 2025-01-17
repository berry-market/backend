package com.berry.payment.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossCancelReqDto {
  private String cancelReason;
  private Integer cancelAmount;
}
