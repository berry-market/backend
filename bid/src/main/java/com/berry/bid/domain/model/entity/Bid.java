package com.berry.bid.domain.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "bid")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "bidder_id", nullable = false)
    private Long bidderId;

    @Column(name = "successful_bid_price", nullable = false)
    private Integer successfulBidPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private Bid(Long postId, Long bidderId, Integer successfulBidPrice, LocalDateTime createdAt) {
        this.postId = postId;
        this.bidderId = bidderId;
        this.successfulBidPrice = successfulBidPrice;
        this.createdAt = createdAt;
    }

    public static Bid create(Long postId, Long bidderId, Integer successfulBidPrice) {
        return new Bid(postId, bidderId, successfulBidPrice, LocalDateTime.now());
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
