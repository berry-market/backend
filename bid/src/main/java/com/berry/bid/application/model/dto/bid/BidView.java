package com.berry.bid.application.model.dto.bid;

import com.berry.bid.domain.model.entity.Bid;
import com.berry.bid.infrastructure.model.dto.DeliveryInternalView;
import com.berry.bid.infrastructure.model.dto.PostInternalView;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class BidView {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SearchRequest {
        private Long bidderId;
        private Long minPrice;
        private Long maxPrice;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime startDate;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime endDate;

        private String sortField;
        private String sortOrder;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private final Long bidId;
        private final String productName;
        private final String productURL;
        private final String deliveryStatus;
        private final Long bidderId;
        private final Integer successfulBidPrice;
        private final LocalDateTime createdAt;
        private final Boolean hasAddress;
        private final Boolean hasReview;

        public static Response from(Bid bid, PostInternalView.Response post, DeliveryInternalView.Response delivery) {
            return new Response(
                    bid.getId(),
                    post.getProductName(),
                    post.getProductImage(),
                    delivery.getStatus(),
                    bid.getBidderId(),
                    bid.getSuccessfulBidPrice(),
                    bid.getCreatedAt(),
                    bid.getHasAddress(),
                    bid.getHasReview()
            );
        }

    }
}
