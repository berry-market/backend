package com.berry.bid.application.repository;

import com.berry.bid.application.model.dto.bid.BidView;
import com.berry.bid.domain.model.entity.Bid;
import com.berry.bid.domain.model.entity.QBid;
import com.berry.bid.domain.repository.BidRepository;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.berry.bid.domain.model.entity.QBid.bid;

@Repository
@RequiredArgsConstructor
public class BidRepositoryImpl implements BidRepository {

    private final BidJpaRepository bidJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final static QBid qbid = bid;

    @Override
    public void save(Bid bid) {
        bidJpaRepository.save(bid);
    }

    @Override
    public Optional<Bid> findById(Long id) {
        return bidJpaRepository.findById(id);
    }

    @Override
    public PageImpl<Bid> getBids(BidView.SearchRequest request, Pageable pageable) {

        BooleanBuilder filterConditions = buildConditions(request);
        List<OrderSpecifier<?>> sortConditions = createSortConditions(pageable.getSort());

        JPAQuery<Bid> query = queryFactory.selectFrom(bid)
                .where(filterConditions)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        sortConditions.forEach(query::orderBy);
        long totalCount = queryFactory.select(bid.count())
                .from(bid)
                .where(filterConditions)
                .fetchOne();

        List<Bid> results = query.fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }


    private Predicate byBidderId(Long bidderId) {
        return bidderId != null ? bid.bidderId.eq(bidderId) : null;
    }

    private Predicate byStartDate(LocalDateTime startDate) {
        return startDate != null ? bid.createdAt.goe(startDate) : null;
    }

    private Predicate byEndDate(LocalDateTime endDate) {
        return endDate != null ? bid.createdAt.loe(endDate) : null;
    }

    private BooleanBuilder buildConditions(BidView.SearchRequest request) {
        BooleanBuilder builder = new BooleanBuilder();

        // 개별 조건 추가
        if (request.getBidderId() != null) {
            builder.and(byBidderId(request.getBidderId()));
        }
        if (request.getStartDate() != null) {
            builder.and(byStartDate(request.getStartDate()));
        }
        if (request.getEndDate() != null) {
            builder.and(byEndDate(request.getEndDate()));
        }

        return builder;
    }

    private OrderSpecifier<?> bySuccessfulBidPrice(Order order) {
        return new OrderSpecifier<>(order, bid.successfulBidPrice);
    }

    private OrderSpecifier<?> byCreatedAt(Order order) {
        return new OrderSpecifier<>(order, bid.createdAt);
    }

    private OrderSpecifier<?> byBidderId(Order order) {
        return new OrderSpecifier<>(order, bid.bidderId);
    }

    private List<OrderSpecifier<?>> createSortConditions(Sort sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        sort.forEach(order -> {
            String property = order.getProperty(); // 정렬 필드
            Order direction = order.isAscending() ? Order.ASC : Order.DESC; // 정렬 방향

            switch (property) {
                case "successfulBidPrice":
                    orderSpecifiers.add(bySuccessfulBidPrice(direction));
                    break;
                case "createdAt":
                    orderSpecifiers.add(byCreatedAt(direction));
                    break;
                case "bidderId":
                    orderSpecifiers.add(byBidderId(direction));
                    break;
                default:
                    throw new CustomApiException(ResErrorCode.BAD_REQUEST,"정렬 조건이 맞지 않습니다.");
            }
        });

        return orderSpecifiers;
    }

}
