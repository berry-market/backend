package com.berry.bid.application.service;

import com.berry.bid.application.model.cache.BidChat;
import com.berry.bid.application.model.dto.bidchat.BidChatCreate;
import com.berry.bid.application.model.event.UserEvent;
import com.berry.bid.application.service.message.BidChatProducerService;
import com.berry.bid.domain.repository.BidChatRepository;
import com.berry.bid.domain.service.BidChatService;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidChatServiceImpl implements BidChatService {

    private static final String bidChatKey = "post:";
    private final BidChatRepository bidChatRepository;
    private final BidChatProducerService bidChatProducerService;

    @Override
    @Transactional
    public BidChat createBidChat(Long postId, BidChatCreate.Request request, Long bidderId) {
        BidChat bidChat = BidChat.of(bidderId, request.getAmount());

        if (validateBidChat(postId, request)) {
            bidChatRepository.saveToSortedSet(bidChatKey + postId, bidChat);
            renewPoints(postId, bidChat);
        } else {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "입찰가가 정상적으로 설정되지 않았습니다.");
        }

        return bidChat;
    }

    @Override
    public List<BidChat> getBidChats(Long postId) {
        return bidChatRepository.findAll();
    }

    private void renewPoints(Long postId, BidChat bidChat) {
        updatePoints(UserEvent.Bidding.from(bidChat));
        Optional<BidChat> latestBidChat = bidChatRepository.getHighestPrice(bidChatKey + postId);
        if (latestBidChat.isPresent()) {
            updatePoints(UserEvent.Bidding.fromLatest(latestBidChat.orElse(null)));
        }
    }

    private Boolean validateBidChat(Long postId, BidChatCreate.Request request) {
        Optional<BidChat> bidChat = bidChatRepository.getHighestPrice(bidChatKey + postId);
        return bidChat.isEmpty() || request.getAmount() > bidChat.get().getAmount();
    }

    private void updatePoints(UserEvent.Bidding event) {
        bidChatProducerService.sendUserEvent(event);
    }

}
