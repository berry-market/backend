package com.berry.bid.domain.repository;

import com.berry.bid.domain.model.entity.Bid;

public interface BidRepository {

    void save(Bid bid);
}
