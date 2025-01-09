package com.berry.post.application.service.consumer;

import com.berry.post.application.model.event.PostEvent;

public interface PostConsumerService {

  void receiveBidEvent(PostEvent.Price event);

}
