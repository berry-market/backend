package com.berry.payment.application.dto;

public record ConfirmPaymentReqDto(
    String orderId,
    int amount,
    String paymentKey
) {}