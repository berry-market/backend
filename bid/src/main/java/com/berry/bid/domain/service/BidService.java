package com.berry.bid.domain.service;

import com.berry.bid.application.model.dto.bid.BidView;
import com.berry.bid.application.model.event.BidEvent;
import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.domain.model.entity.Bid;
import com.berry.bid.infrastructure.model.dto.PostInternalView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BidService {

    void createBid(PostEvent.Close event);

    Bid getBidById(Long id);

    void putAddress(BidEvent.Delivery event);

    void deleteById(Long id);

    Page<BidView.Response> getBidsWithDetails(BidView.SearchRequest request, Pageable pageable);

    BidView.Response getBidDetails(Long bidId);

}
