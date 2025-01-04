package com.berry.delivery.application;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.delivery.domain.model.Delivery;
import com.berry.delivery.domain.repository.DeliveryRepository;
import com.berry.delivery.presentation.dto.DeliveryDto;
import com.berry.delivery.presentation.dto.request.DeliveryCreateRequest;
import com.berry.delivery.presentation.dto.request.DeliveryUpdateRequest;
import com.berry.delivery.presentation.dto.response.DeliverySearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public DeliveryDto createDelivery(DeliveryCreateRequest req) {
        DeliveryDto dto = DeliveryCreateRequest.toDto(req);

        Delivery deliveryEntity = deliveryRepository.save(dto.toEntity(dto));
        return DeliveryDto.from(deliveryEntity);
    }

    @Transactional
    public Page<DeliveryDto> getAllDelivery(Pageable pageable) {
        Page<Delivery> deliveries = deliveryRepository.findAll(pageable);
        return deliveries.map(DeliveryDto::from);
    }

    @Transactional
    public DeliverySearchResponse getDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(()->new CustomApiException(ResErrorCode.BAD_REQUEST,"존재하지 않는 배송입니다."));
        return DeliverySearchResponse.from(delivery);
    }

    @Transactional
    public void deleteDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(()->new CustomApiException(ResErrorCode.BAD_REQUEST,"존재하지 않는 배송입니다."));

        delivery.markAsDeleted();
    }
}
