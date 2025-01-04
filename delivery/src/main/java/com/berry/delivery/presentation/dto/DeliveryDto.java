package com.berry.delivery.presentation.dto;

import com.berry.delivery.domain.model.Delivery;
import com.berry.delivery.domain.model.DeliveryStatus;
import java.sql.Timestamp;
import lombok.Builder;

@Builder
public record DeliveryDto(
        Long deliveryId,
        Long receiverId,
        Long senderId,
        Long bidId,
        String address,
        DeliveryStatus status,
        Timestamp created_at,
        String created_by,
        Timestamp updated_at,
        String updated_by,
        Timestamp deleted_at,
        String deleted_by,
        Boolean deleted_yn
) {
    //entity -> dto
    public Delivery toEntity(DeliveryDto dto){
        return Delivery.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .bidId(bidId)
                .address(address)
                .status(status)
                .build();
    }

    //dto -> entity
    public static DeliveryDto from(Delivery entity){
        return DeliveryDto.builder()
                .deliveryId(entity.getDeliveryId())
                .receiverId(entity.getReceiverId())
                .senderId(entity.getSenderId())
                .bidId(entity.getBidId())
                .address(entity.getAddress())
                .status(entity.getStatus())
                .created_at(Timestamp.valueOf(entity.getCreatedAt()))
                .created_by(entity.getCreatedBy())
                .updated_at(Timestamp.valueOf(entity.getUpdatedAt()))
                .updated_by(entity.getUpdatedBy())
                .deleted_at(Timestamp.valueOf(entity.getDeletedAt()))
                .deleted_by(entity.getDeletedBy())
                .deleted_yn(entity.isDeletedYN())
                .build();
    }
}
