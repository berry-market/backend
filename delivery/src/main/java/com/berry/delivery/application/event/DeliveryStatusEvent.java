package com.berry.delivery.application.event;

import com.berry.delivery.domain.model.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryStatusEvent {
    private Long bidId;
    private DeliveryStatus status;
}