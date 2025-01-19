package com.berry.bid.infrastructure.service.message;

import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.application.service.message.BidChatConsumerService;
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

    @Override
    @KafkaListener(topics = "bid-events", containerFactory = "postListenerContainerFactory")
    public void closePostEvent(PostEvent.Close postEvent) {
        log.info("PostEvent 받아옴 = "+ postEvent.getPostId());
        bidService.createBid(postEvent);
    }

}
