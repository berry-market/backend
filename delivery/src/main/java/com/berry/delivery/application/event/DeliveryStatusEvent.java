package com.berry.delivery.application.event;

import com.berry.delivery.domain.model.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryStatusEvent {
    private Long bidId;
    private DeliveryStatus status;
}