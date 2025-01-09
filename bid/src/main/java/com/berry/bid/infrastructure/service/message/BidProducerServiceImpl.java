package com.berry.bid.infrastructure.service.message;

import com.berry.bid.application.model.event.DeliveryEvent;
import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.application.service.message.BidProducerService;
import com.berry.bid.infrastructure.model.enums.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidProducerServiceImpl implements BidProducerService {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    @Override
    public void sendPostEvent(PostEvent.Price event) {
        kafkaTemplate.send(KafkaTopic.POST_EVENTS.getTopicName(), event);
    }

    @Override
    public void sendDeliveryEvent(DeliveryEvent.Create event) {
        kafkaTemplate.send(KafkaTopic.DELIVERY_EVENTS.getTopicName(), event);
    }

}
