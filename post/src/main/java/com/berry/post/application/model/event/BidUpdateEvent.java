package com.berry.post.application.model.event;


import com.berry.post.domain.model.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class BidUpdateEvent {
  @Getter
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class PostBidUpdateEvent {

    Long postId;
    String productStatus;

    public static PostBidUpdateEvent from(Post post) {
      return new PostBidUpdateEvent(post.getId(),post.getProductStatus()+"");
    }
  }

}
