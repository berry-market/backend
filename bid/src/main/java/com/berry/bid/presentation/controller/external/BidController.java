package com.berry.bid.presentation.controller.external;

import com.berry.bid.application.model.dto.bid.BidView;
import com.berry.bid.domain.model.entity.Bid;
import com.berry.bid.domain.service.BidService;
import com.berry.bid.infrastructure.client.PostClient;
import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.bid.infrastructure.model.dto.PostInternalView;
import com.berry.common.role.RoleCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @GetMapping("/bids/{bidId}")
    @RoleCheck({"MEMBER","ADMIN"})
    public ApiResponse<BidView.Response> bidView(@PathVariable Long bidId) {
        Bid bid = bidService.getBidById(bidId);
        PostInternalView.Response postResponse = bidService.getPostDetails(bidId);
        BidView.Response response = BidView.Response.from(bid,postResponse);
        return ApiResponse.OK(ResSuccessCode.READ, response);
    }

    @DeleteMapping("/bids/{bidId}")
    public ApiResponse<Void> deleteBid(@PathVariable Long bidId) {
        bidService.deleteById(bidId);
        return ApiResponse.OK(ResSuccessCode.DELETED);
    }

}
