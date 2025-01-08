package com.berry.bid.application.service;

import com.berry.bid.application.model.cache.BidChat;
import com.berry.bid.application.model.dto.bidchat.BidChatCreate;
import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.application.model.event.UserEvent;
import com.berry.bid.application.service.consumer.BidChatProducerService;
import com.berry.bid.domain.repository.BidChatRepository;
import com.berry.bid.domain.service.BidChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidChatServiceImpl implements BidChatService {

    private static final String bidChatKey = "post:";
    private final BidChatRepository bidChatRepository;
    private final BidChatProducerService bidChatProducerService;

    //TODO : error exception
    @Override
    @Transactional
    public BidChat createBidChat(Long postId, BidChatCreate.Request request, Long bidderId) {
        BidChat bidChat = BidChat.of(bidderId, request.getAmount());

        if (validateBidChat(postId, request)) {
            bidChatRepository.saveToSortedSet(bidChatKey + postId, bidChat);
            renewPoints(postId, bidChat);
        } else {
            throw new RuntimeException();
        }

        return bidChat;
    }

    private void renewPoints(Long postId, BidChat bidChat) {
        updatePoints(UserEvent.Bidding.from(bidChat));
        Optional<BidChat> latestBidChat = bidChatRepository.getHighestPrice(bidChatKey + postId);
        updatePoints(UserEvent.Bidding.fromLatest(latestBidChat.orElse(null)));
    }

    private Boolean validateBidChat(Long postId, BidChatCreate.Request request) {
        Optional<BidChat> bidChat = bidChatRepository.getHighestPrice(bidChatKey + postId);
        return bidChat.isEmpty() || request.getAmount() > bidChat.get().getAmount();
    }

    private void updatePoints(UserEvent.Bidding event) {
        bidChatProducerService.sendUserEvent(event);
    }

}
