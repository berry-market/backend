package com.berry.delivery.application.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BidCompletionEvent {
    private Long bidId;
    private Long sellerId;  // 판매자 ID
    private Long winnerId;  // 낙찰자 ID
}