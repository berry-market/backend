package com.berry.bid.domain.service;

import com.berry.bid.application.model.event.BidEvent;
import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.domain.model.entity.Bid;
import com.berry.bid.infrastructure.model.dto.PostInternalView;

public interface BidService {

    void createBid(PostEvent.Close event);

    Bid getBidById(Long id);

    PostInternalView.Response getPostDetails(Long bidId);

    void putAddress(BidEvent.Delivery event);

    void deleteById(Long id);

}
