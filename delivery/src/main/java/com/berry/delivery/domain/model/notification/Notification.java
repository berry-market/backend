package com.berry.delivery.domain.model.notification;

import com.berry.common.auditor.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification")
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column
    private Long winnerId;

    @Column
    private Long sellerId;

    @Column(nullable = false)
    private String message;
}
