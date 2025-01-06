package com.berry.bid.domain.model.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bid {

    private Long id;
    private Long postId;
    private Long bidderId;
    private Integer successfulBidPrice;
    private LocalDateTime createdAt;
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

    private void delete() {
        this.deletedAt = LocalDateTime.now();
    }

}
