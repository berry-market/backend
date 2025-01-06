package com.berry.delivery.presentation.dto.request;

import com.berry.delivery.domain.model.DeliveryStatus;
import com.berry.delivery.presentation.dto.DeliveryDto;

public record DeliveryUpdateRequest(
        Long receiverId,
        Long senderId,
        Long bidId,
        String address,
        DeliveryStatus status
) {
    public static DeliveryDto toDto(DeliveryUpdateRequest req) {
        return DeliveryDto.builder()
                .receiverId(req.receiverId())
                .senderId(req.senderId())
                .bidId(req.bidId())
                .address(req.address())
                .status(req.status())
                .build();
    }
}
