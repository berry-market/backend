package com.berry.bid.application.dto.bidchat;

import lombok.Getter;

import java.time.LocalDateTime;

public class BidChatCreate {

    @Getter
    public static class Request{

        private final int amount;

        public Request(int amount) {
            this.amount = amount;
        }

    }

    public static class Response{

        private final int amount;
        private final String nickname;
        private final LocalDateTime bidTime;

        private Response(int amount, String nickname, LocalDateTime bidTime) {
            this.amount = amount;
            this.nickname = nickname;
            this.bidTime = bidTime;
        }

        public static Response of(int amount, String nickname, LocalDateTime bidTime) {
            return new Response(amount, nickname, bidTime);
        }
    }

}
