package com.berry.delivery.domain.repository;

import com.berry.delivery.domain.model.Delivery;
import com.berry.delivery.domain.model.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepositoryCustom {
    Page<Delivery> searchDelivery(Long deliveryId, Long receiverId, Long senderId, Long bidId, DeliveryStatus status, Pageable paeable);
}
