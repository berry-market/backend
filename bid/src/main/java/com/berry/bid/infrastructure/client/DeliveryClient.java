package com.berry.bid.infrastructure.client;

import com.berry.bid.infrastructure.model.dto.DeliveryInternalView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @GetMapping("api/v1/deliveries")
    DeliveryInternalView.Response getDelivery(@RequestParam Long bidId);

}
