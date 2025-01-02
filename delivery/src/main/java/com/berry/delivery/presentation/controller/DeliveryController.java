package com.berry.delivery.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.delivery.application.DeliveryService;
import com.berry.delivery.presentation.dto.request.DeliveryCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createDelivery(@Valid @RequestBody DeliveryCreateRequest req) {
        deliveryService.createDelivery(req);
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.CREATED, "배송이 생성되었습니다."));
    }

}
