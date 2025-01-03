package com.berry.delivery.application;

import com.berry.delivery.domain.model.Delivery;
import com.berry.delivery.presentation.dto.DeliveryDto;
import com.berry.delivery.presentation.dto.request.DeliveryCreateRequest;
import com.berry.delivery.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
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

}
