package com.berry.delivery.domain.model;

import com.berry.common.auditor.BaseEntity;
import com.berry.delivery.presentation.dto.request.DeliveryUpdateRequest;
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

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long bidId;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public void updatedelivery(DeliveryUpdateRequest req) {
        if (req.receiverId() != null) {
            this.receiverId = req.receiverId();
        }
        if (req.senderId() != null) {
            this.senderId = req.senderId();
        }
        if (req.bidId() != null) {
            this.bidId = req.bidId();
        }
        if (req.address() != null) {
            this.address = req.address();
        }
        if (req.status() != null) {
            this.status = req.status();
        }
    }
}
