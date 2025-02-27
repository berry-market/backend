package com.berry.delivery.presentation.controller.delivery;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.delivery.application.service.delivery.DeliveryService;
import com.berry.delivery.domain.model.delivery.DeliveryStatus;
import com.berry.delivery.presentation.dto.DeliveryDto;
import com.berry.delivery.presentation.dto.request.delivery.DeliveryCreateRequest;
import com.berry.delivery.presentation.dto.request.delivery.DeliveryUpdateRequest;
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
    public ResponseEntity<ApiResponse<?>> getAllDelivery(
            Long deliveryId,
            Long receiverId,
            Long senderId,
            Long bidId,
            DeliveryStatus deliveryStatus,
            Pageable pageable,
            String sortType
    ) {
        Page<DeliveryDto> deliveries = deliveryService.getAllDelivery(deliveryId,receiverId,senderId,bidId,deliveryStatus,pageable,sortType);
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, deliveries));
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<ApiResponse<?>> getDelivery(@PathVariable Long deliveryId) {
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, deliveryService.getDelivery(deliveryId)));
    }

    @PatchMapping("/{deliveryId}")
    public ResponseEntity<ApiResponse<?>> updateDelivery(@PathVariable Long deliveryId, @Valid @RequestBody DeliveryUpdateRequest req) {
        deliveryService.updateDelivery(deliveryId, req);
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.UPDATED, "배송이 수정되었습니다."));
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<ApiResponse<?>> deleteDelivery(@PathVariable Long deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.DELETED, "배송이 삭제되었습니다."));
    }

    //Bid용
    @GetMapping("/bid/{bidId}")
    public ResponseEntity<ApiResponse<?>> getBid(@PathVariable Long bidId) {
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, deliveryService.getBid(bidId)));
    }

}
