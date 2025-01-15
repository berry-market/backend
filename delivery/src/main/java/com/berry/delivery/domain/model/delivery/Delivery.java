package com.berry.delivery.domain.model.delivery;

import com.berry.common.auditor.BaseEntity;
import com.berry.delivery.presentation.dto.request.delivery.DeliveryUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery")
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @Column(nullable = false, updatable = false)
    private Long receiverId;

    @Column(nullable = false, updatable = false)
    private Long senderId;

    @Column(nullable = false, updatable = false)
    private Long bidId;

    @Column
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public void updatedelivery(DeliveryUpdateRequest req) {
        if (req.address() != null) {
            this.address = req.address();
        }
        if (req.status() != null) {
            this.status = req.status();
        }
    }
}
