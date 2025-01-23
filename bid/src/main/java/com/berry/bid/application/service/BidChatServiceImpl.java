package com.berry.bid.application.service;

import com.berry.bid.application.model.cache.BidChat;
import com.berry.bid.application.model.dto.bidchat.BidChatCreate;
import com.berry.bid.application.model.dto.bidchat.BidChatView;
import com.berry.bid.application.model.event.PostEvent;
import com.berry.bid.application.model.event.UserEvent;
import com.berry.bid.application.service.message.BidChatProducerService;
import com.berry.bid.application.service.message.BidProducerService;
import com.berry.bid.domain.repository.BidChatRepository;
import com.berry.bid.domain.service.BidChatService;
import com.berry.bid.infrastructure.client.PostClient;
import com.berry.bid.infrastructure.client.UserClient;
import com.berry.bid.infrastructure.model.dto.PostInternalView;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidChatServiceImpl implements BidChatService {

    private static final String bidChatKey = "post:";
    private static final String bidChatImmediatePriceKey = "ImmediatePricePost:";
    private final BidChatRepository bidChatRepository;
    private final BidProducerService bidProducerService;
    private final BidChatProducerService bidChatProducerService;
    private final UserClient userClient;
    private final PostClient postClient;

    @Override
    @Transactional
    public void startBidChat(PostEvent.Update event) {
        BidChat bidChat = BidChat.of(event.getWriterId(), event.getStartedPrice());
        bidChatRepository.saveToSortedSet(bidChatKey + event.getPostId(), bidChat);
        bidChatRepository.saveImmediatePrice(bidChatImmediatePriceKey + event.getPostId(), event.getImmediatePrice());
    }

    @Override
    @Transactional
    public BidChat createBidChat(Long postId, BidChatCreate.Request request, Long bidderId) {
        Integer amount = request.getAmount();
        BidChat bidChat = BidChat.of(bidderId,amount);
        Integer immediatePrice = bidChatRepository.findImmediatePrice(bidChatImmediatePriceKey + postId);

        validateConditions(postId, request, bidChat);
        bidChatRepository.saveToSortedSet(bidChatKey + postId, bidChat);
        renewPoints(postId, bidChat);

        if (validateImmediatePrice(immediatePrice,amount)) {
            bidProducerService.sendPostEvent(PostEvent.Price.of(postId,amount));
        }

        return bidChat;
    }

    private void validateConditions(Long postId, BidChatCreate.Request request, BidChat bidChat) {
        existsBidChat(postId);
        validateBidder(postId, bidChat);
        validateBidChat(postId, request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BidChatView.Response> getBidChats(Long postId) {
        List<BidChat> bidChats = bidChatRepository.findAll(bidChatKey + postId);
        return toDtoList(bidChats);
    }

    private List<BidChatView.Response> toDtoList(List<BidChat> bidChats) {
        return bidChats.stream()
                .map(bidChat -> {
                    String nickname = userClient.getUserById(bidChat.getBidderId())
                            .getData()
                            .getNickname();
                    return BidChatView.Response.of(nickname, bidChat.getAmount(), bidChat.getCreatedAt());
                })
                .collect(Collectors.toList());
    }

    private void renewPoints(Long postId, BidChat bidChat) {
        updatePoints(UserEvent.Bidding.from(bidChat));
        Optional<BidChat> latestBidChat = bidChatRepository.getHighestPrice(bidChatKey + postId);
        validateBidder(postId, bidChat);
        if (latestBidChat.isPresent()) {
            updatePoints(UserEvent.Bidding.fromLatest(latestBidChat.orElse(null)));
        }
    }

    private void validateBidder(Long postId, BidChat bidChat) {
        PostInternalView.Response response = postClient.getPost(postId);
        if (!response.getWriterId().equals(bidChat.getBidderId())) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "게시자는 입찰에 참여할 수 없습니다.");
        }
    }

    private Boolean validateImmediatePrice(Integer immediatePrice, Integer requestPrice) {
        if (immediatePrice.equals(requestPrice)) {
            return true;
        } else if (immediatePrice < requestPrice) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "입찰가가 즉시구매가보다 높을 수 없습니다.");
        } else{
            return false;
        }
    }

    private void validateBidChat(Long postId, BidChatCreate.Request request) {
        Optional<BidChat> bidChat = bidChatRepository.getHighestPrice(bidChatKey + postId);
        if (request.getAmount() > bidChat.orElseThrow().getAmount()) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "입찰이 정상적으로 요청되지 않았습니다.");
        }
        ;
    }

    private void existsBidChat(Long postId) {
        Optional<BidChat> bidChat = bidChatRepository.getHighestPrice(bidChatKey + postId);
        if (bidChat.isEmpty()) {
            throw new CustomApiException(ResErrorCode.SERVICE_UNAVAILABLE, "시작하지 않은 입찰입니다.");
        }
    }

    private void updatePoints(UserEvent.Bidding event) {
        bidChatProducerService.sendUserEvent(event);
    }

}
