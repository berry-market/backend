package com.berry.payment.application.dto;

import java.time.LocalDateTime;

public record PaymentGetResDto(
    Long id,
    String paymentKey,
    int amount,
    String paymentMethod,
    String paymentStatus,
    LocalDateTime createdAt
) {}
