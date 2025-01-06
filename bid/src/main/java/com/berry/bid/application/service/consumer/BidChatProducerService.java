package com.berry.bid.application.service.consumer;

import com.berry.bid.application.model.event.UserEvent;

public interface BidChatProducerService {

    void sendUserEvent(UserEvent.Bidding event);

}
