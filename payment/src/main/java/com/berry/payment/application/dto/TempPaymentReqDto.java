package com.berry.payment.application.dto;

import lombok.Getter;

public record TempPaymentReqDto(
    String orderId,
    int amount
) {}