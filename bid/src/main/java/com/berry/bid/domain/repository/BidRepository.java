package com.berry.bid.domain.repository;

import com.berry.bid.application.model.dto.bid.BidView;
import com.berry.bid.domain.model.entity.Bid;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BidRepository {

    // jpa repository
    void save(Bid bid);

    Optional<Bid> findById(Long id);

    // query dsl

    PageImpl<Bid> getBids(BidView.SearchRequest request, Pageable pageable);

}
