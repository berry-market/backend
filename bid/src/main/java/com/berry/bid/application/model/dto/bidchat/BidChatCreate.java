package com.berry.bid.application.model.dto.bidchat;

import lombok.*;

import java.time.LocalDateTime;

public class BidChatCreate {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Request {

        private int amount;

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
