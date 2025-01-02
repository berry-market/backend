package com.berry.bid.application.dto;

import lombok.Getter;

public class BidCreate {

    @Getter
    public static class Request{
        private int amount;
    }

    @Getter
    public static class Response{}

}
