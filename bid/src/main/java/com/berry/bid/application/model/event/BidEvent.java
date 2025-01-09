package com.berry.bid.application.model.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class BidEvent {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Delivery{
        private final Long bidId;
        private final String deliveryStatus;
    }

}
