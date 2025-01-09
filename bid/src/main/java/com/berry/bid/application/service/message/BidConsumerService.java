package com.berry.bid.application.service.message;

import com.berry.bid.application.model.event.BidEvent;

public interface BidConsumerService {

    void putAddress(BidEvent.Delivery event);
}
