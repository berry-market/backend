package com.berry.bid.application.model.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserEvent {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Bidding {
        Long userId;
        Integer amount;

        public Bidding of(Long userId, Integer amount) {
            return new Bidding(userId, amount);
        }
    }

}
