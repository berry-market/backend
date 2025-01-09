package com.berry.bid.domain.repository;

import com.berry.bid.domain.model.entity.Bid;

import java.util.Optional;

public interface BidRepository {

    // jpa repository
    void save(Bid bid);

    Optional<Bid> findById(Long id);

    // query dsl


}
