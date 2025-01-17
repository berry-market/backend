package com.berry.bid.infrastructure.client;

import com.berry.bid.infrastructure.model.dto.DeliveryInternalView;
import com.berry.bid.infrastructure.model.dto.PostInternalView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @GetMapping("/deliveries/{bidId}")
    DeliveryInternalView.Response getDelivery(@PathVariable Long bidId);

}
