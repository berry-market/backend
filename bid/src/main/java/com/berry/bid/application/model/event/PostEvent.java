package com.berry.bid.application.model.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PostEvent {
    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Close{
        private final Long postId;
        private final Long writerId;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Price{
        private final Long bidId;
        private final Integer successfulBidPrice;

        public static Price of(Long bidId, Integer price) {
            return new Price(bidId, price);
        }

    }

}
