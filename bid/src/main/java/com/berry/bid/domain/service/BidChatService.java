package com.berry.bid.domain.service;

import com.berry.bid.application.dto.bidchat.BidChatCreate;

public interface BidChatService {

    void createBidChat(Long postId, BidChatCreate.Request request,Long bidderId);

}
