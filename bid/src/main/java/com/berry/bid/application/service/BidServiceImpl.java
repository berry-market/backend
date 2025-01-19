package com.berry.bid.application.service;

import com.berry.bid.application.model.dto.bid.BidView;
import com.berry.bid.application.model.event.BidEvent;
import com.berry.bid.application.model.event.DeliveryEvent;
import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.application.service.message.BidProducerService;
import com.berry.bid.domain.model.entity.Bid;
import com.berry.bid.domain.repository.BidChatRepository;
import com.berry.bid.domain.repository.BidRepository;
import com.berry.bid.domain.service.BidService;
import com.berry.bid.infrastructure.client.DeliveryClient;
import com.berry.bid.infrastructure.client.PostClient;
import com.berry.bid.infrastructure.model.dto.DeliveryInternalView;
import com.berry.bid.infrastructure.model.dto.PostInternalView;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private static final String bidChatKey = "post:";
    private final BidProducerService bidProducerService;
    private final BidChatRepository bidChatRepository;
    private final BidRepository bidRepository;
    private final PostClient postClient;
    private final DeliveryClient deliveryClient;

    @Override
    @Transactional
    public void createBid(PostEvent.Close event) {

        Bid bid = eventToBid(event);
        bidRepository.save(bid);
        bidProducerService.sendPostEvent(bidToPrice(bid));

        DeliveryEvent.Create deliveryEvent = DeliveryEvent.Create
                .of(
                        bid.getId(),
                        getSellerIdByPost(bid.getId()),
                        bid.getBidderId()
                );
        bidProducerService.sendDeliveryEvent(deliveryEvent);
    }

    private PostInternalView.Response getPostDetails(Long bidId) {
        return postClient.getPost(bidId);
    }

    @Override
    @Transactional
    public BidView.Response getBidDetails(Long bidId) {
        Bid bid = getBidById(bidId);
        PostInternalView.Response response = getPostDetails(bidId);
        DeliveryInternalView.Response delivery = getDeliveryDetails(bidId);
        return BidView.Response.from(bid, response, delivery);
    }

    @Override
    @Transactional
    public void putAddress(BidEvent.Delivery event) {
        Bid bid = bidRepository.findById(event.getBidId()).orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 낙찰 건이 존재하지 않습니다."));
        if (event.getDeliveryStatus().equals("STARTED")) {
            bid.putAddress();
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bidRepository.findById(id).ifPresent(Bid::delete);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<BidView.Response> getBidsWithDetails(BidView.SearchRequest request, Pageable pageable) {

        Page<Bid> bidPage = bidRepository.getBids(request, pageable);

        List<BidView.Response> responses = bidPage.getContent().stream()
                .map(bid -> {
                    PostInternalView.Response post = postClient.getPost(bid.getPostId());
                    DeliveryInternalView.Response delivery = deliveryClient.getDelivery(bid.getId());
                    return BidView.Response.from(bid, post, delivery);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, bidPage.getTotalElements());
    }

    private Long getSellerIdByPost(Long bidId) {
        return getPostDetails(bidId).getWriterId();
    }

    @Override
    @Transactional(readOnly = true)
    public Bid getBidById(Long id) {
        return bidRepository.findById(id)
                .orElseThrow(
                        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당하는 낙찰 정보가 존재하지 않습니다")
                );
    }

    private Bid eventToBid(PostEvent.Close event) {
        Integer amount = bidChatRepository.getHighestPrice(bidChatKey + event.getPostId())
                .orElseThrow(
                        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당하는 입찰 정보가 존재하지 않습니다")
                )
                .getAmount();
        return Bid.create(event.getPostId(), event.getWriterId(), amount);
    }

    private PostEvent.Price bidToPrice(Bid bid) {
        return PostEvent.Price.of(bid.getId(), bid.getSuccessfulBidPrice());
    }

    private DeliveryInternalView.Response getDeliveryDetails(Long bidId) {
        return deliveryClient.getDelivery(bidId);
    }


}
