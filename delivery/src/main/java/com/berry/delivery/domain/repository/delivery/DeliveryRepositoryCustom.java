package com.berry.delivery.domain.repository.delivery;

import com.berry.delivery.domain.model.delivery.Delivery;
import com.berry.delivery.domain.model.delivery.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepositoryCustom {
    Page<Delivery> searchDelivery(Long deliveryId, Long receiverId, Long senderId, Long bidId, DeliveryStatus status, Pageable paeable);
}
