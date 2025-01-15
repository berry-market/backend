package com.berry.delivery.presentation.dto;

import com.berry.delivery.domain.model.notification.Notification;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record NotificationDto(
        Long notificationId,
        Long winnerId,
        Long sellerId,
        String message,
        LocalDateTime created_at,
        String created_by,
        LocalDateTime updated_at,
        String updated_by,
        LocalDateTime deleted_at,
        String deleted_by,
        Boolean deleted_yn
) {
    public static NotificationDto from(Notification entity){
        return NotificationDto.builder()
                .notificationId(entity.getNotificationId())
                .winnerId(entity.getWinnerId())
                .sellerId(entity.getSellerId())
                .message(entity.getMessage())
                .created_at(entity.getCreatedAt())
                .created_by(entity.getCreatedBy())
                .updated_at(entity.getUpdatedAt())
                .updated_by(entity.getUpdatedBy())
                .deleted_at(entity.getDeletedAt())
                .deleted_by(entity.getDeletedBy())
                .deleted_yn(entity.isDeletedYN())
                .build();
    }

    public Notification toEntity(NotificationDto dto){
        return Notification.builder()
                .winnerId(winnerId)
                .sellerId(sellerId)
                .message(message)
                .build();
    }
}
