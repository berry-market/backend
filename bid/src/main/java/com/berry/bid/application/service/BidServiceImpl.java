package com.berry.bid.application.service;

import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.domain.model.entity.Bid;
import com.berry.bid.domain.repository.BidChatRepository;
import com.berry.bid.domain.repository.BidRepository;
import com.berry.bid.domain.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final BidChatRepository bidChatRepository;
    private final BidRepository bidRepository;
    private static final String bidChatKey = "post:";

    @Override
    @Transactional
    public void createBid(PostEvent.Close event) {
        bidRepository.save(eventToBid(event));
    }

    // delivery 로 event 넘겨주기
    // post 로 event 넘겨주기
    //TODO : error exception
    private Bid eventToBid(PostEvent.Close event){
        Integer amount = bidChatRepository.getHighestPrice(bidChatKey + event.getPostId())
                .orElseThrow(RuntimeException::new)
                .getAmount();
        return Bid.create(event.getPostId(),event.getWriterId(),amount);
    }

}
