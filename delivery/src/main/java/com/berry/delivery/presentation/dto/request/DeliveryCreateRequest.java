package com.berry.delivery.presentation.dto.request;

import com.berry.delivery.domain.model.DeliveryStatus;
import com.berry.delivery.presentation.dto.DeliveryDto;

public record DeliveryCreateRequest(
        Long deliveryId,
        Long receiverId,
        Long senderId,
        Long bidId,
        String address,
        String status
        ) {
    public static DeliveryDto toDto(DeliveryCreateRequest request){
        return DeliveryDto.builder()
                .deliveryId(request.deliveryId())
                .receiverId(request.receiverId())
                .senderId(request.senderId())
                .bidId(request.bidId())
                .address(request.address())
                .status(DeliveryStatus.READY)
                .build();
    }
}
