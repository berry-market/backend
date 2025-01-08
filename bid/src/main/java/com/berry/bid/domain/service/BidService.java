package com.berry.bid.domain.service;

import com.berry.bid.application.model.event.PostEvent;

public interface BidService {

    void createBid(PostEvent.Close event);

}
