package com.berry.bid.application.model.dto.bid;

import com.berry.bid.domain.model.entity.Bid;
import com.berry.bid.infrastructure.model.dto.PostInternalView;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class BidView {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SearchRequest {
        private Long bidderId;
        private Long minPrice;
        private Long maxPrice;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String sortField;
        private String sortOrder;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private final Long bidId;
        private final Long bidderId;
        private final Integer successfulBidPrice;
        private final LocalDateTime createdAt;
        private final Boolean hasAddress;
        private final String productName;
        private final String productImage;

        public static Response from(Bid bid, PostInternalView.Response response) {
            return new Response(
                    bid.getId(),
                    bid.getBidderId(),
                    bid.getSuccessfulBidPrice(),
                    bid.getCreatedAt(),
                    bid.getHasAddress(),
                    response.getProductName(),
                    response.getProductImage()
            );
        }

    }
}
