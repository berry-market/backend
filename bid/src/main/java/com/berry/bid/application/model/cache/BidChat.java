package com.berry.bid.application.model.cache;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash("BidChat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BidChat {

    private Long bidderId;
    private Integer amount;
    private LocalDateTime createdAt;

    private BidChat(Long bidderId, Integer amount) {
        this.bidderId = bidderId;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    public static BidChat of(Long bidderId, Integer amount) {
        return new BidChat(bidderId, amount);
    }

}
