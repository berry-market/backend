package com.berry.bid.presentation.controller.external;

import com.berry.bid.application.dto.bidchat.BidChatCreate;
import com.berry.bid.domain.service.BidChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BidChatController {

    private final BidChatService bidChatService;

    @MessageMapping("/api/bids")
    @SendTo("/topic")
    public void createBidChat(@RequestParam Long postId,
                              @RequestBody BidChatCreate.Request bidChatCreate) {
        Long bidderId = 1L;
        bidChatService.createBidChat(postId, bidChatCreate, bidderId);
    }

}
