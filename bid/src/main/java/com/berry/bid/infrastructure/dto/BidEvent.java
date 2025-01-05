package com.berry.bid.infrastructure.dto;

import lombok.Getter;

@Getter
public class BidEvent {
    private final String userId;
    private final int points;

    public BidEvent(String userId, int points) {
        this.userId = userId;
        this.points = points;
    }
}
