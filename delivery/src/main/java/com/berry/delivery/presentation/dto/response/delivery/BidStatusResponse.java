package com.berry.delivery.presentation.dto.response.delivery;

import com.berry.delivery.domain.model.delivery.Delivery;
import com.berry.delivery.domain.model.delivery.DeliveryStatus;
import lombok.Builder;

@Builder
public record BidStatusResponse(
        DeliveryStatus status
) {
    public static BidStatusResponse from(Delivery delivery) {
        return BidStatusResponse.builder()
                .status(delivery.getStatus())
                .build();
    }
}
