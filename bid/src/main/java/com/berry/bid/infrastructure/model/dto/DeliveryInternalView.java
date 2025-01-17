package com.berry.bid.infrastructure.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class DeliveryInternalView {

    @Getter
    @AllArgsConstructor
    public static class Response {
        Long deliveryId;
        Long receiverId;
        Long senderId;
        Long bidId;
        String address;
        String status;
        LocalDateTime created_at;
        String created_by;
        LocalDateTime updated_at;
        String updated_by;
        LocalDateTime deleted_at;
        String deleted_by;
        Boolean deleted_yn;
    }

}
