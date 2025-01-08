package com.berry.post.application.service.producer;

import com.berry.post.application.event.BidCreateEvent.PostBidCreateEvent;
import com.berry.post.application.event.BidUpdateEvent.PostBidUpdateEvent;

public interface PostProducerService {

  void sendPostCreateEvent(PostBidCreateEvent event);

  void sendPostUpdateEvent(PostBidUpdateEvent event);

}
