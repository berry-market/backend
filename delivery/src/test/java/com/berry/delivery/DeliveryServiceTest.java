package com.berry.delivery;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.delivery.application.service.delivery.DeliveryService;
import com.berry.delivery.application.service.notification.NotificationService;
import com.berry.delivery.application.service.producer.DeliveryStatusProducer;
import com.berry.delivery.domain.model.delivery.Delivery;
import com.berry.delivery.domain.model.delivery.DeliveryStatus;
import com.berry.delivery.domain.repository.delivery.DeliveryRepository;
import com.berry.delivery.presentation.dto.request.delivery.DeliveryCreateRequest;
import com.berry.delivery.presentation.dto.request.delivery.DeliveryUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private DeliveryStatusProducer deliveryStatusProducer;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private DeliveryService deliveryService;

    private Delivery sampleDelivery;
    private DeliveryCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        sampleDelivery = Delivery.builder()
                .deliveryId(1L)
                .receiverId(100L)
                .senderId(200L)
                .bidId(300L)
                .status(DeliveryStatus.READY)
                .build();

        createRequest = new DeliveryCreateRequest(
                1L,
                100L,
                200L,
                300L
        );
    }

    @Test
    @DisplayName("배송 생성 성공 테스트")
    void createDeliverySuccess() {
        // Given
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(sampleDelivery);

        // When
        var result = deliveryService.createDelivery(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.deliveryId()).isEqualTo(sampleDelivery.getDeliveryId());
        assertThat(result.status()).isEqualTo(DeliveryStatus.READY);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    @DisplayName("배송 상태 업데이트 성공 테스트 - READY to STARTED")
    void updateDeliveryStatusSuccess() {
        // Given
        String address = "서울특별시 강남구"; // 실제 테스트에 사용할 주소 값
        DeliveryUpdateRequest updateRequest = new DeliveryUpdateRequest(address, DeliveryStatus.STARTED);
        when(deliveryRepository.findByDeliveryId(1L)).thenReturn(Optional.of(sampleDelivery));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(sampleDelivery);

        // When
        deliveryService.updateDelivery(1L, updateRequest);

        // Then
        verify(deliveryStatusProducer, times(1))
                .publishDeliveryStatus(sampleDelivery.getBidId(), DeliveryStatus.STARTED);
        verify(notificationService, times(1))
                .winnerCreateNotification(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("완료된 배송 상태 변경 시 예외 발생")
    void updateCompletedDeliveryThrowsException() {
        // Given
        String address = "서울특별시 강남구";
        sampleDelivery.updatedelivery(new DeliveryUpdateRequest(address,DeliveryStatus.DONE));
        DeliveryUpdateRequest updateRequest = new DeliveryUpdateRequest(address,DeliveryStatus.STARTED);
        when(deliveryRepository.findByDeliveryId(1L)).thenReturn(Optional.of(sampleDelivery));

        // When & Then
        assertThatThrownBy(() -> deliveryService.updateDelivery(1L, updateRequest))
                .isInstanceOf(CustomApiException.class)
                .hasMessageContaining("완료된 배송은 상태를 변경할 수 없습니다");
    }

    @Test
    @DisplayName("페이지네이션 조회 테스트")
    void getAllDeliveryWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Delivery> deliveryPage = new PageImpl<>(List.of(sampleDelivery));
        when(deliveryRepository.searchDelivery(any(), any(), any(), any(), any(), any()))
                .thenReturn(deliveryPage);

        // When
        var result = deliveryService.getAllDelivery(
                null, null, null, null, null,
                pageable, "createdAtDesc"
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(deliveryRepository, times(1))
                .searchDelivery(any(), any(), any(), any(), any(), any());
    }
}