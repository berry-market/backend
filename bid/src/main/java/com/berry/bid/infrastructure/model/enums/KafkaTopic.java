package com.berry.bid.infrastructure.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaTopic {
    POST_EVENTS("post-events"),
    USER_EVENTS("user-events");

    private final String topicName;

}