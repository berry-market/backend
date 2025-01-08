package com.berry.delivery.presentation.dto.request;

import com.berry.delivery.domain.model.DeliveryStatus;
import com.berry.delivery.presentation.dto.DeliveryDto;

public record DeliveryCreateRequest(
        Long deliveryId,
        Long receiverId,
        Long senderId,
        Long bidId
        ) {
}
