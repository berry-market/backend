package com.berry.bid.infrastructure.service.message;

import com.berry.bid.application.model.event.UserEvent;
import com.berry.bid.application.service.message.BidChatProducerService;
import com.berry.bid.infrastructure.model.enums.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidChatProducerServiceImpl implements BidChatProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendUserEvent(UserEvent.Bidding event) {
        kafkaTemplate.send(KafkaTopic.USER_EVENTS.getTopicName(), event);
    }

}
