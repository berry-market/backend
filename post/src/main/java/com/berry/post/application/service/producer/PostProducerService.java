package com.berry.post.application.service.producer;

import com.berry.post.application.event.BidEvent;

public interface PostProducerService {

  void sendPostEvent(BidEvent.PostBidEvent event);

}
