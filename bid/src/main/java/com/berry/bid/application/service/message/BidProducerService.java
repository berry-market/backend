package com.berry.bid.application.service.message;

import com.berry.bid.application.model.event.DeliveryEvent;
import com.berry.bid.application.model.event.PostEvent;

public interface BidProducerService {

    void sendPostEvent(PostEvent.Price event);

    void sendDeliveryEvent(DeliveryEvent.Create event);
}
