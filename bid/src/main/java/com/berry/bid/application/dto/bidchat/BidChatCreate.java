package com.berry.bid.application.dto.bidchat;

import lombok.Getter;

public class BidChatCreate {

    @Getter
    public static class Request{

        private final int amount;

        public Request(int amount) {
            this.amount = amount;
        }

    }

    public static class Response{}
}
