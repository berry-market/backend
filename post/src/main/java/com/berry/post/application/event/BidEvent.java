package com.berry.post.application.event;

import com.berry.post.domain.model.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class BidEvent {

  @Getter
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class PostBidEvent {
    Long Id;
    Long writerId;

    public static PostBidEvent from(Post post) {
      return new PostBidEvent(post.getId(), post.getWriterId());
    }
  }

}
