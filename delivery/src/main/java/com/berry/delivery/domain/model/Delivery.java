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

    @Setter
    @Column(nullable = false)
    private Long receiverId;

    @Setter
    @Column(nullable = false)
    private Long senderId;

    @Setter
    @Column(nullable = false)
    private Long bidId;

    @Setter
    @Column(nullable = false)
    private String address;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

}
