package com.berry.bid.application.model.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class DeliveryEvent {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Create {
        private final Long bidId;
        private final Long sellerId;
        private final Long buyerId;

        public static Create of(Long id, Long sellerId, Long buyerId) {
            return new Create(id, sellerId, buyerId);
        }
    }

}
