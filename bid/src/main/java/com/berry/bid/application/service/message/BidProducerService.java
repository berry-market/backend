package com.berry.bid.application.service.message;

import com.berry.bid.application.model.event.PostEvent;

public interface BidProducerService {

    void sendPostEvent(PostEvent.Price event);
}
