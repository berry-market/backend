package com.berry.bid.domain.service;

import com.berry.bid.application.dto.bidchat.BidChatCreate;
import com.berry.bid.application.model.cache.BidChat;

public interface BidChatService {

    BidChat createBidChat(Long postId, BidChatCreate.Request request, Long bidderId);

    Boolean validateBidChat(Long postId, BidChatCreate.Request request);

}
