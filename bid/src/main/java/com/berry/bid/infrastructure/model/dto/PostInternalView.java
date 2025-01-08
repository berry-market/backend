package com.berry.bid.infrastructure.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PostInternalView {

    @Getter
    @AllArgsConstructor
    public static class Response{
        private Long postCategoryId;
        private Long writerId;
        private String productName;
        private String productContent;
        private Integer immediatePrice;
        private Integer startedPrice;
        private LocalDateTime auctionStartedAt;
        private LocalDateTime auctionEndedAt;
        private String productStatus;
        private String deliveryMethod;
        private Integer deliveryFee;
        private String productImage;
        private Integer likeCount;
        private Integer viewCount;
    }

}
