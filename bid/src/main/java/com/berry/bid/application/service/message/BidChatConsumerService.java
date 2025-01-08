package com.berry.bid.application.service.message;

import com.berry.bid.application.model.event.PostEvent;

public interface BidChatConsumerService {

    void closePostEvent(PostEvent.Close postEvent);

}
