package com.berry.bid.domain.service;

import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.domain.model.entity.Bid;

public interface BidService {

    void createBid(PostEvent.Close event);

    Bid getBidById(Long id);

}
