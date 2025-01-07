package com.berry.delivery.presentation.dto.request;

import com.berry.delivery.domain.model.DeliveryStatus;

public record DeliveryUpdateRequest(
        String address,
        DeliveryStatus status
) {
}
