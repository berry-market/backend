package com.berry.delivery.application;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.common.role.RoleCheck;
import com.berry.delivery.domain.model.Delivery;
import com.berry.delivery.domain.model.DeliveryStatus;
import com.berry.delivery.domain.repository.DeliveryRepository;
import com.berry.delivery.presentation.dto.DeliveryDto;
import com.berry.delivery.presentation.dto.request.DeliveryCreateRequest;
import com.berry.delivery.presentation.dto.request.DeliveryUpdateRequest;
import com.berry.delivery.presentation.dto.response.DeliverySearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public DeliveryDto createDelivery(DeliveryCreateRequest req) {

        Delivery delivery = Delivery.builder()
                .deliveryId(req.deliveryId())
                .receiverId(req.receiverId())
                .senderId(req.senderId())
                .bidId(req.bidId())
                .status(DeliveryStatus.READY)
                .build();

        return DeliveryDto.from(deliveryRepository.save(delivery));
    }

    @Transactional
    public Page<DeliveryDto> getAllDelivery(
            Long deliveryId,
            Long receiverId,
            Long senderId,
            Long bidId,
            DeliveryStatus deliveryStatus,
            Pageable pageable,
            String sortType
    ) {
        // 페이지 사이즈 제한
        int[] allowedPageSizes = {10, 30, 50};
        int pageSize = pageable.getPageSize();

        // 허용되지 않은 페이지 사이즈일 경우 기본값 10으로 설정
        if (!Arrays.stream(allowedPageSizes).anyMatch(size -> size == pageSize)) {
            pageable = PageRequest.of(pageable.getPageNumber(), 10, pageable.getSort());
        }

        // 정렬 로직 추가
        Sort sort = determineSort(sortType);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Delivery> deliveries = deliveryRepository.searchDelivery(
                deliveryId,
                receiverId,
                senderId,
                bidId,
                deliveryStatus,
                pageable
        );

        return deliveries.map(DeliveryDto::from);
    }

    // 정렬 타입에 따른 Sort 결정 메서드
    private Sort determineSort(String sortType) {
        if (StringUtils.isEmpty(sortType)) {
            // 기본 정렬: 생성일 내림차순
            return Sort.by("createdAt").descending();
        }

        switch (sortType) {
            case "createdAtAsc":
                return Sort.by("createdAt").ascending();
            case "updatedAtDesc":
                return Sort.by("updatedAt").descending();
            case "updatedAtAsc":
                return Sort.by("updatedAt").ascending();
            default:
                return Sort.by("createdAt").descending();
        }
    }

    @Transactional
    public DeliverySearchResponse getDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.BAD_REQUEST, "존재하지 않는 배송입니다."));
        return DeliverySearchResponse.from(delivery);
    }

    @Transactional
    public void updateDelivery(Long deliveryId, DeliveryUpdateRequest req) {
        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.BAD_REQUEST, "존재하지 않는 배송입니다."));

        validateStatusTransition(delivery.getStatus(), req.status());

        delivery.updatedelivery(req);

        deliveryRepository.save(delivery);
    }

    @Transactional
    @RoleCheck(value = {"ADMIN"})
    public void deleteDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.BAD_REQUEST, "존재하지 않는 배송입니다."));

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
