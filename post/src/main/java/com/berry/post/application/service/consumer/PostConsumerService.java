package com.berry.post.application.service.consumer;

import com.berry.post.application.event.BidEventMessage.BidPostEvent;

public interface PostConsumerService {

  void receiveBidEvent(BidPostEvent event);

}
