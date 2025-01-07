package com.berry.delivery.domain.repository;

import com.berry.delivery.domain.model.Delivery;
import com.berry.delivery.domain.model.DeliveryStatus;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

import static com.berry.delivery.domain.model.QDelivery.delivery;


public class DeliveryRepositoryImpl extends QuerydslRepositorySupport implements DeliveryRepositoryCustom {

    public DeliveryRepositoryImpl() {
        super(Delivery.class);
    }

    @Override
    public Page<Delivery> searchDelivery(Long deliveryId, Long receiverId, Long senderId, Long bidId, DeliveryStatus deliveryStatus, Pageable pageable) {

        JPQLQuery<Delivery> query = from(delivery)
                .where(
                        containDeliveryId(deliveryId),
                        containReceiverId(receiverId),
                        containSenderId(senderId),
                        containBidId(bidId),
                        containStatus(deliveryStatus),
                        delivery.deletedYN.eq(false)
                );
        List<Delivery> deliveries = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(deliveries, pageable, query.fetchCount());
    }

    private Predicate containDeliveryId(Long deliveryId) {
        return deliveryId != null ? delivery.deliveryId.eq(deliveryId) : null;
    }

    private Predicate containReceiverId(Long receiverId) {
        return receiverId != null ? delivery.receiverId.eq(receiverId) : null;
    }

    private Predicate containSenderId(Long senderId) {
        return senderId != null ? delivery.senderId.eq(senderId) : null;
    }

    private Predicate containBidId(Long bidId) {
        return bidId != null ? delivery.bidId.eq(bidId) : null;
    }

    private Predicate containStatus(DeliveryStatus deliveryStatus) {
        return Optional.ofNullable(deliveryStatus)
                .map(status -> delivery.status.eq(status))
                .orElse(null);
    }
}
