package com.berry.post.application.service.producer;

import com.berry.post.application.model.event.PostEvent;
import com.berry.post.application.model.event.PostEvent.Status;
import com.berry.post.infrastructure.model.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostProducerServiceImpl implements PostProducerService {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void sendPostCloseEvent(PostEvent.Status event) {
    kafkaTemplate.send(KafkaTopic.BID_CLOSE_EVENTS.getTopicName(), event);
  }

  @Override
  public void sendPostActiveEvent(PostEvent.Status event) {
    kafkaTemplate.send(KafkaTopic.BID_ACTIVE_EVENTS.getTopicName(), event);
  }
}
