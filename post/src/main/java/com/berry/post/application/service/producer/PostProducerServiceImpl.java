package com.berry.post.application.service.producer;

import com.berry.post.application.model.event.PostEvent;
import com.berry.post.infrastructure.model.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostProducerServiceImpl implements PostProducerService {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void sendPostCreateEvent(PostEvent.Close event) {
    kafkaTemplate.send(KafkaTopic.BID_CLOSE_EVENTS.getTopicName(), event);
  }

  @Override
  public void sendPostUpdateEvent(PostEvent.Update event) {
    kafkaTemplate.send(KafkaTopic.BID_UPDATE_EVENTS.getTopicName(), event);
  }
}
