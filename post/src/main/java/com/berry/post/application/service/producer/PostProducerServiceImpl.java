package com.berry.post.application.service.producer;

import com.berry.post.application.event.BidEvent.PostBidEvent;
import com.berry.post.infrastructure.model.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostProducerServiceImpl implements PostProducerService {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void sendPostEvent(PostBidEvent event) {
    kafkaTemplate.send(KafkaTopic.BID_EVENTS.getTopicName(), event);
  }
}
