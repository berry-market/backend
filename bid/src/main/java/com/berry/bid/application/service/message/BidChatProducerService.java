package com.berry.bid.application.service.message;

import com.berry.bid.application.model.event.UserEvent;

public interface BidChatProducerService {

    void sendUserEvent(UserEvent.Bidding event);

}
