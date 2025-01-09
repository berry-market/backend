package com.berry.bid.application.service;

import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.application.service.message.BidProducerService;
import com.berry.bid.domain.model.entity.Bid;
import com.berry.bid.domain.repository.BidChatRepository;
import com.berry.bid.domain.repository.BidRepository;
import com.berry.bid.domain.service.BidService;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private static final String bidChatKey = "post:";
    private final BidProducerService bidProducerService;
    private final BidChatRepository bidChatRepository;
    private final BidRepository bidRepository;

    @Override
    @Transactional
    public void createBid(PostEvent.Close event) {
        Bid bid = eventToBid(event);
        bidRepository.save(bid);
        bidProducerService.sendPostEvent(bidToPrice(bid));
    }

    @Override
    public Bid getBidById(Long id) {
        return bidRepository.findById(id)
                .orElseThrow(
                        ()->new CustomApiException(ResErrorCode.NOT_FOUND,"해당하는 낙찰 정보가 존재하지 않습니다")
                );
    }

    // delivery 로 event 넘겨주기
    private Bid eventToBid(PostEvent.Close event) {
        Integer amount = bidChatRepository.getHighestPrice(bidChatKey + event.getPostId())
                .orElseThrow(
                        ()->new CustomApiException(ResErrorCode.NOT_FOUND,"해당하는 입찰 정보가 존재하지 않습니다")
                )
                .getAmount();
        return Bid.create(event.getPostId(), event.getWriterId(), amount);
    }

    private PostEvent.Price bidToPrice(Bid bid) {
        return PostEvent.Price.of(bid.getId(), bid.getSuccessfulBidPrice());
    }

}
