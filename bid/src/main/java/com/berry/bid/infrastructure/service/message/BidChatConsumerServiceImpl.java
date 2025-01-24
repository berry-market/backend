package com.berry.bid.infrastructure.service.message;

import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.application.service.message.BidChatConsumerService;
import com.berry.bid.domain.service.BidChatService;
import com.berry.bid.domain.service.BidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BidChatConsumerServiceImpl implements BidChatConsumerService {

    private final BidService bidService;
    private final BidChatService bidChatService;

    @Override
    @KafkaListener(topics = "bid-events-close", containerFactory = "postCloseListenerContainerFactory")
    public void closePostEvent(PostEvent.Close postEvent) {
        bidService.createBid(postEvent);
    }

    @Override
    @KafkaListener(topics = "bid-events-update", containerFactory = "postUpdateListenerContainerFactory")
    public void updatePostEvent(PostEvent.Update postEvent) {
        if(postEvent.getStatus().equals("ACTIVE")){
            bidChatService.startBidChat(postEvent);
        }
    }

}
