package com.berry.delivery.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.delivery.application.DeliveryService;
import com.berry.delivery.domain.model.DeliveryStatus;
import com.berry.delivery.presentation.dto.DeliveryDto;
import com.berry.delivery.presentation.dto.request.DeliveryCreateRequest;
import com.berry.delivery.presentation.dto.response.DeliverySearchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllDelivery(Pageable pageable) {
        Page<DeliveryDto> deliveries = deliveryService.getAllDelivery(pageable);
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, deliveries));
    }
   @GetMapping("/{deliveryId}")
    public ResponseEntity<ApiResponse<?>> getDelivery(@PathVariable Long deliveryId) {
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, deliveryService.getDelivery(deliveryId)));
   }
}
