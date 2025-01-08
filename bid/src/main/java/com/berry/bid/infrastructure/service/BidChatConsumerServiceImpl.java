package com.berry.bid.infrastructure.service;

import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.application.service.consumer.BidChatConsumerService;
import com.berry.bid.domain.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidChatConsumerServiceImpl implements BidChatConsumerService {

    private final BidService bidService;

    @Override
    @KafkaListener(topics = "post-topic")
    public void closePostEvent(PostEvent.Close postEvent) {
        bidService.createBid(postEvent);
    }

}
