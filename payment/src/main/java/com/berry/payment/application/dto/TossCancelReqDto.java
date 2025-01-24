package com.berry.payment.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public record TossCancelReqDto(
    String cancelReason,
    Integer cancelAmount
) {}