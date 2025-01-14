package com.berry.delivery.presentation.dto.request.notification;

public record NotificationCreateRequest(
        Long notificationId,
        Long winnerId,
        Long sellerId,
        String message
) {
}
