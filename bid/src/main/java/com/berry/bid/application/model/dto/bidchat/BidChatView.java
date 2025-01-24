package com.berry.bid.application.model.dto.bidchat;

import com.berry.bid.application.model.cache.BidChat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BidChatView {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response{
        private String nickname;
        private Integer amount;
        private LocalDateTime createdAt;

        public static Response of(String nickname, Integer amount, LocalDateTime createdAt) {
            return new Response(nickname, amount, createdAt);
        }

    }

}
