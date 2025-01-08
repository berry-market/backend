package com.berry.bid.application.repository;

import com.berry.bid.domain.model.entity.Bid;
import com.berry.bid.domain.model.entity.QBid;
import com.berry.bid.domain.repository.BidRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BidRepositoryImpl implements BidRepository {

    private final BidJpaRepository bidJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final static QBid qbid = QBid.bid;

    @Override
    public void save(Bid bid) {
        bidJpaRepository.save(bid);
    }
}
