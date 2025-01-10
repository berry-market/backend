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
    public static class Close{
        private Long postId;
        private Long writerId;

        public static PostEvent.Close from(Post post) {
            return new PostEvent.Close(post.getId(), post.getWriterId());
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Update{
        private Long postId;
        private ProductStatus status;

        public static PostEvent.Update from(Post post) {
            return new PostEvent.Update(post.getId(), post.getProductStatus());
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
