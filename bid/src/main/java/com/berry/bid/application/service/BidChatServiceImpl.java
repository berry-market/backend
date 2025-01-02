package com.berry.bid.application.service;

import com.berry.bid.application.dto.bidchat.BidChatCreate;
import com.berry.bid.application.model.cache.BidChat;
import com.berry.bid.domain.repository.BidChatRepository;
import com.berry.bid.domain.service.BidChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidChatServiceImpl implements BidChatService {

    private final BidChatRepository bidChatRepository;
    private static String bidChatKey = "post:";

    @Override
    public void createBidChat(Long postId, BidChatCreate.Request request, Long bidderId) {
        BidChat bidChat = BidChat.of(bidderId, request.getAmount());
        bidChatRepository.saveToSortedSet(bidChatKey + postId , bidChat);
    }
}
