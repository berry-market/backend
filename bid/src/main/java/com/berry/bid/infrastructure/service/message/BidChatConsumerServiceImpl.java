package com.berry.bid.infrastructure.service.message;

import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.application.service.message.BidChatConsumerService;
import com.berry.bid.domain.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidChatConsumerServiceImpl implements BidChatConsumerService {

    private final BidService bidService;

    @Override
    @KafkaListener(topics = "bid-events")
    public void closePostEvent(PostEvent.Close postEvent) {
        bidService.createBid(postEvent);
    }

}
