package com.berry.bid.domain.service;

import com.berry.bid.application.model.cache.BidChat;
import com.berry.bid.application.model.dto.bidchat.BidChatCreate;
import com.berry.bid.application.model.dto.bidchat.BidChatView;

import java.util.List;

public interface BidChatService {

    BidChat createBidChat(Long postId, BidChatCreate.Request request, Long bidderId);

    List<BidChatView.Response> getBidChats(Long postId);

}
