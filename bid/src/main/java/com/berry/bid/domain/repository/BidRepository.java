package com.berry.bid.domain.repository;

import com.berry.bid.domain.model.entity.Bid;

public interface BidRepository {

    // jpa repository
    void save(Bid bid);

    // query dsl


}
