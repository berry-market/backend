package com.berry.delivery.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery")
public class Delivery {

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

}
