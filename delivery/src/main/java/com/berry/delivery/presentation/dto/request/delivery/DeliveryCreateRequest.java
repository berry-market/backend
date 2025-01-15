package com.berry.delivery.presentation.dto.request.delivery;

public record DeliveryCreateRequest(
        Long deliveryId,
        Long receiverId,
        Long senderId,
        Long bidId
        ) {
}
