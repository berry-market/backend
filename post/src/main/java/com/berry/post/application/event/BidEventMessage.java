package com.berry.post.application.event;

import com.berry.post.application.event.BidEvent.PostBidEvent;
import com.berry.post.domain.model.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BidEventMessage {

  @Getter
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class BidPostEvent {
    Long postId;
    Integer successfulBidPrice;

    public static BidPostEvent to(BidPostEvent event) {
      return new BidPostEvent(event.getPostId(), event.getSuccessfulBidPrice());
    }
  }
}
