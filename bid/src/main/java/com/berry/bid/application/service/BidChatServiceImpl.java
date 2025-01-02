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
    private static final String bidChatKey = "post:";

    //TODO : error exception
    @Override
    public BidChat createBidChat(Long postId, BidChatCreate.Request request, Long bidderId) {
        BidChat bidChat = BidChat.of(bidderId, request.getAmount());
        if(validateBidChat(postId,request)) {
            bidChatRepository.saveToSortedSet(bidChatKey + postId , bidChat);
        } else{
            throw new RuntimeException();
        }
        return bidChat;
    }

    @Override
    public Boolean validateBidChat(Long postId, BidChatCreate.Request request) {
        BidChat bidChat = bidChatRepository.getHighestPrice(bidChatKey + postId)
                .orElseThrow();
        return request.getAmount() > bidChat.getAmount();
    }


}
