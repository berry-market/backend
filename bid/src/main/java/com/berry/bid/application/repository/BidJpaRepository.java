package com.berry.bid.application.repository;

import com.berry.bid.domain.model.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidJpaRepository extends JpaRepository<Bid, Long> {

}
