package com.berry.delivery.presentation.dto.request.notification;

public record NotifiactionCreateRequest(
        Long notificationId,
        Long winnerId,
        Long sellerId,
        String message
) {
}
