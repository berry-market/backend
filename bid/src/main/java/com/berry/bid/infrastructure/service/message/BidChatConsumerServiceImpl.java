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
    @KafkaListener(topics = "bid-close-events", containerFactory = "postCloseListenerContainerFactory")
    public void closePostEvent(PostEvent.Close postEvent) {
        bidService.createBid(postEvent);
        log.info("Bid closed id : {}", postEvent.getPostId());
    }

    @Override
    @KafkaListener(topics = "bid-active-events", containerFactory = "postUpdateListenerContainerFactory")
    public void updatePostEvent(PostEvent.Update postEvent) {
        if(postEvent.getStatus().equals("ACTIVE")){
            bidChatService.startBidChat(postEvent);
            log.info("bid chat started id : {}", postEvent.getPostId());
        }
    }

}
