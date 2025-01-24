package com.berry.bid.presentation.controller.external;

import com.berry.bid.application.model.dto.bidchat.BidChatCreate;
import com.berry.bid.application.model.cache.BidChat;
import com.berry.bid.application.model.dto.bidchat.BidChatView;
import com.berry.bid.domain.service.BidChatService;
import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.common.role.RoleCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BidChatController {

    private final BidChatService bidChatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/api/v1/posts/{postId}/bids-chat")
    @RoleCheck("MEMBER")
    public void createBidChat(@DestinationVariable Long postId,
                              @RequestHeader("X-UserId") Long userId,
                              @RequestHeader("X-Nickname") String nickname,
                              @Payload BidChatCreate.Request bidChatCreate) {

        BidChat bidchat = bidChatService.createBidChat(postId, bidChatCreate, userId);
        BidChatCreate.Response response = BidChatCreate.Response.of(bidchat.getAmount(), nickname, bidchat.getCreatedAt());

        messagingTemplate.convertAndSend("/topic/bids-chat/" + postId, response);

    }

    @GetMapping("/api/v1/posts/{postId}/bids-chat")
    public ApiResponse<List<BidChatView.Response>> getChat(@PathVariable Long postId) {
        List<BidChatView.Response> bidChats =bidChatService.getBidChats(postId);
        return ApiResponse.OK(ResSuccessCode.READ,bidChats);
    }

}
