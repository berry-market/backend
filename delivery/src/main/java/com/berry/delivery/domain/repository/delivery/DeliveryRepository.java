package com.berry.delivery.domain.repository.delivery;

import java.util.Optional;
import com.berry.delivery.domain.model.delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long>,DeliveryRepositoryCustom {
    Optional<Delivery> findByDeliveryId(Long deliveryId);

    Optional<Delivery> findBybidId(Long bidId);
}
