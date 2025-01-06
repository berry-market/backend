package com.berry.bid.application.model.dto.bid;

import lombok.Getter;

public class BidCreate {

    @Getter
    public static class Request{
        private int amount;
    }

    @Getter
    public static class Response{}

}
