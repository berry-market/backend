package com.berry.bid.application.dto.bidchat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public class BidChatCreate {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Request {

        private final int amount;

    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {

        private final int amount;
        private final String nickname;
        private final LocalDateTime bidTime;

        public static Response of(int amount, String nickname, LocalDateTime bidTime) {
            return new Response(amount, nickname, bidTime);
        }

    }

}
