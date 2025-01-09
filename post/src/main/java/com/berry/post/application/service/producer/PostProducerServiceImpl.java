package com.berry.post.application.service.producer;

import com.berry.post.application.model.event.BidCreateEvent.PostBidCreateEvent;
import com.berry.post.application.model.event.BidUpdateEvent.PostBidUpdateEvent;
import com.berry.post.infrastructure.model.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostProducerServiceImpl implements PostProducerService {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void sendPostCreateEvent(PostBidCreateEvent event) {
    kafkaTemplate.send(KafkaTopic.BID_EVENTS.getTopicName(), "create", event);
  }

  @Override
  public void sendPostUpdateEvent(PostBidUpdateEvent event) {
    kafkaTemplate.send(KafkaTopic.BID_EVENTS.getTopicName(), "update", event);
  }
}
