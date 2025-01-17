package com.berry.bid.infrastructure.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class DeliveryInternalView {

    @Getter
    @AllArgsConstructor
    public static class Response{
        private String deliveryStatus;
    }
}
