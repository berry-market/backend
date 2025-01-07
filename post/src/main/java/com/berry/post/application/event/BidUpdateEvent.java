package com.berry.post.application.event;


import com.berry.post.domain.model.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class BidUpdateEvent {
  @Getter
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class PostBidUpdateEvent {
    String productStatus;

    public static PostBidUpdateEvent from(Post post) {
      return new PostBidUpdateEvent(post.getProductStatus()+"");
    }
  }

}
