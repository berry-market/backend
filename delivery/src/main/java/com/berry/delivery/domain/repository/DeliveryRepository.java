package com.berry.delivery.domain.repository;

import java.util.Optional;
import com.berry.delivery.domain.model.Delivery;
import com.berry.delivery.domain.model.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long>,DeliveryRepositoryCustom {
    Optional<Delivery> findByDeliveryId(Long deliveryId);
}
