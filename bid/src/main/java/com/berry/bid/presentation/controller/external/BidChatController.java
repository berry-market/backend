package com.berry.bid.presentation.controller.external;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class BidChatController {

    @MessageMapping("/")
    @SendTo("/topic")
    public void asd(){
    }

}
