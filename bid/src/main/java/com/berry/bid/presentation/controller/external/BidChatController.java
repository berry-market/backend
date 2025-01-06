package com.berry.bid.presentation.controller.external;

import com.berry.bid.application.dto.bidchat.BidChatCreate;
import com.berry.bid.application.model.cache.BidChat;
import com.berry.bid.domain.service.BidChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BidChatController {

    private final BidChatService bidChatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/api/v1/posts/{postId}/bids-chat")
    public void createBidChat(@DestinationVariable Long postId,
                              @Payload BidChatCreate.Request bidChatCreate) {

        //TODO : user 검증 시 바꾸기
        Long bidderId = 1L;
        String bidderNickname = "bidder";

        BidChat bidchat = bidChatService.createBidChat(postId, bidChatCreate, bidderId);
        BidChatCreate.Response response = BidChatCreate.Response.of(bidchat.getAmount(), bidderNickname, bidchat.getCreatedAt());

        messagingTemplate.convertAndSend("/topic/bids-chat/" + postId, response);

    }

}
