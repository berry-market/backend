package com.berry.post.application.event;

import com.berry.post.domain.model.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class BidCreateEvent {

  @Getter
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class PostBidCreateEvent {
    Long postId;
    Long writerId;

    public static PostBidCreateEvent from(Post post) {
      return new PostBidCreateEvent(post.getId(), post.getWriterId());
    }
  }

}
