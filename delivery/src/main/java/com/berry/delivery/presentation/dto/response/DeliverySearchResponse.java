package com.berry.delivery.presentation.dto.response;

import com.berry.delivery.domain.model.Delivery;
import com.berry.delivery.domain.model.DeliveryStatus;
import lombok.Builder;

@Builder
public record DeliverySearchResponse(
        Long deliveryId,
        Long receiverId,
        Long senderId,
        Long bidId,
        String address,
        DeliveryStatus status
) {
    public static DeliverySearchResponse from(Delivery delivery) {
        return DeliverySearchResponse.builder()
                .deliveryId(delivery.getDeliveryId())
                .receiverId(delivery.getReceiverId())
                .senderId(delivery.getSenderId())
                .bidId(delivery.getBidId())
                .address(delivery.getAddress())
                .status(delivery.getStatus())
                .build();
    }
}
