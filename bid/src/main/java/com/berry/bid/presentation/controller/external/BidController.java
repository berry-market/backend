package com.berry.bid.presentation.controller.external;

import com.berry.bid.application.model.dto.bid.BidView;
import com.berry.bid.domain.model.entity.Bid;
import com.berry.bid.domain.service.BidService;
import com.berry.bid.infrastructure.client.PostClient;
import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.bid.infrastructure.model.dto.PostInternalView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;
    private final PostClient postClient;

    @GetMapping("/bids/{bidId}")
    public ApiResponse<BidView.Response> bidView(@PathVariable Long bidId) {
        Bid bid = bidService.getBidById(bidId);
        PostInternalView.Response postResponse = postClient.getPost(bid.getPostId());
        BidView.Response response = BidView.Response.from(bid,postResponse);
        return ApiResponse.OK(ResSuccessCode.READ, response);
    }

}
