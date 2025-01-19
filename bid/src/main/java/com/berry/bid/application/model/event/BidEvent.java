package com.berry.bid.application.model.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BidEvent {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Delivery{
        private Long bidId;
        private String deliveryStatus;
    }

}
