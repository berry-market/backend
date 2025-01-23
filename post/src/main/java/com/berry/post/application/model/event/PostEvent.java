package com.berry.post.application.model.event;

import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostEvent {
    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Status {
        private Long postId;
        private ProductStatus status;
        private Long writerId;
        private Integer startedPrice;

        public static Status from(Post post) {
            return new Status(post.getId(), post.getProductStatus(), post.getWriterId(), post.getStartedPrice());
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Price{
        private Long postId;
        private Integer successfulBidPrice;
    }

}
