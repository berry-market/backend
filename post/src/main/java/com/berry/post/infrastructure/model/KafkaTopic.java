package com.berry.post.infrastructure.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaTopic {
  BID_EVENTS("bid-events");

  private final String topicName;
}
