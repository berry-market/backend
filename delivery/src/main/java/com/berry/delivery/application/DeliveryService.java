package com.berry.delivery.application;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.delivery.domain.model.Delivery;
import com.berry.delivery.domain.model.DeliveryStatus;
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
    public void updateDelivery(Long deliveryId, DeliveryUpdateRequest req) {
        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId)
                .orElseThrow(()->new CustomApiException(ResErrorCode.BAD_REQUEST,"존재하지 않는 배송입니다."));

        validateStatusTransition(delivery.getStatus(), req.status());

        delivery.updatedelivery(req);

        deliveryRepository.save(delivery);
    }

    @Transactional
    public void deleteDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(()->new CustomApiException(ResErrorCode.BAD_REQUEST,"존재하지 않는 배송입니다."));

        delivery.markAsDeleted();
    }

    private void validateStatusTransition(DeliveryStatus currentStatus, DeliveryStatus newStatus) {
        if (currentStatus == DeliveryStatus.DONE) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "완료된 배송은 상태를 변경할 수 없습니다.");
        }

        if (currentStatus == DeliveryStatus.STARTED && newStatus == DeliveryStatus.READY) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "이미 시작된 배송을 준비 상태로 되돌릴 수 없습니다.");
        }

        if (newStatus == DeliveryStatus.DONE && currentStatus != DeliveryStatus.STARTED) {
            throw new CustomApiException(ResErrorCode.BAD_REQUEST, "배송이 시작되지 않은 상태에서는 완료할 수 없습니다.");
        }
    }
}
