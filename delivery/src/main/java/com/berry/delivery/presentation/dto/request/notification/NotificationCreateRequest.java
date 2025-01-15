package com.berry.delivery.presentation.dto.request.notification;

public record NotificationCreateRequest(
        Long notificationId,
        Long winnerId,
        Long sellerId,
        String message
) {
    public static NotificationCreateRequest SellerCreate(Long seller,String message) {
        return new NotificationCreateRequest(null, null, seller, message);
    }

    public static NotificationCreateRequest WinnerCreate(Long winner,String message) {
        return new NotificationCreateRequest(null, winner, null, message);
    }
}
