package com.berry.delivery.presentation.dto.response.notification;

import com.berry.delivery.domain.model.notification.Notification;
import lombok.Builder;

@Builder
public record NotificationSearchResponse(
        Long notificationId,
        Long winnerId,
        Long sellerId,
        String message
) {
    public static NotificationSearchResponse from( Notification notification ) {
        return NotificationSearchResponse.builder()
                .notificationId(notification.getNotificationId())
                .winnerId(notification.getWinnerId())
                .sellerId(notification.getSellerId())
                .message(notification.getMessage())
                .build();
    }
}
