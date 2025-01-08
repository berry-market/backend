package com.berry.post.application.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BidEventMessage {

  @Getter
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class BidPostEvent {
    Long postId;
    Integer successfulBidPrice;
  }
}
