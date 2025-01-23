package com.berry.post.infrastructure.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaTopic {

  BID_CLOSE_EVENTS("bid-close-events"),
  BID_ACTIVE_EVENTS("bid-active-events");

  private final String topicName;
}
