package com.berry.delivery.presentation.dto.request.delivery;

import com.berry.delivery.domain.model.delivery.DeliveryStatus;

public record DeliveryUpdateRequest(
        String address,
        DeliveryStatus status
) {
}
