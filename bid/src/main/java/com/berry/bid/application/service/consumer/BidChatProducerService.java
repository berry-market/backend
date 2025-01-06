package com.berry.bid.application.service.consumer;

import com.berry.bid.application.model.cache.BidChat;

public interface BidChatProducerService {

    void produceBidChat(BidChat bidChat);
}
