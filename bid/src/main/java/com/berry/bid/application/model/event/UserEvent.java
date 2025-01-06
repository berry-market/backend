package com.berry.bid.application.model.event;

import com.berry.bid.application.model.cache.BidChat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserEvent {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Bidding {
        Long userId;
        Integer amount;

        public static Bidding from(BidChat bidChat) {
            return new Bidding(bidChat.getBidderId(), bidChat.getAmount());
        }

        public static Bidding fromLatest(BidChat bidChat) {
            return new Bidding(bidChat.getBidderId(), -bidChat.getAmount());
        }
    }

}
