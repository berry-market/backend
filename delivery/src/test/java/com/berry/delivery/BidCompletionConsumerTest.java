package com.berry.delivery;

import com.berry.delivery.application.event.BidCompletionEvent;
import com.berry.delivery.application.service.consumer.BidCompletionConsumer;
import com.berry.delivery.application.service.delivery.DeliveryService;
import com.berry.delivery.application.service.notification.NotificationService;
import com.berry.delivery.domain.model.delivery.DeliveryStatus;
import com.berry.delivery.presentation.dto.DeliveryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidCompletionConsumerTest {

    @Mock
    private DeliveryService deliveryService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BidCompletionConsumer bidCompletionConsumer;

    private BidCompletionEvent sampleEvent;
    private DeliveryDto sampleDeliveryDto;

    @BeforeEach
    void setUp() {
        sampleEvent = new BidCompletionEvent(
                1L,    // bidId
                100L,  // sellerId
                200L,  // winnerId
                "Sample message"
        );

        sampleDeliveryDto = new DeliveryDto(
                1L,
                200L,
                100L,
                1L,
                "서울특별시 강남구",
                DeliveryStatus.READY,
                LocalDateTime.now(),
                "system",
                LocalDateTime.now(),
                "system",
                null,
                null,
                false
        );
    }

    @Test
    @DisplayName("입찰 완료 이벤트 처리 성공 테스트")
    void consumeBidCompletionSuccess() {
        // Given
        when(deliveryService.createDelivery(any())).thenReturn(sampleDeliveryDto);
        String expectedWinnerMessage = String.format("상품 낙찰자로 선정되었습니다!");
        String expectedSellerMessage = String.format("등록하신 상품이 낙찰되었습니다.");

        // When
        bidCompletionConsumer.consumeBidCompletion(sampleEvent, 0, 0);

        // Then
        verify(deliveryService, times(1)).createDelivery(any());

        // 낙찰자 알림 검증
        verify(notificationService, times(1))
                .winnerCreateNotification(
                        eq(sampleEvent.getWinnerId()),  // winnerId 검증
                        eq(expectedWinnerMessage),      // 메시지 내용 검증
                        eq("당첨자 알림")
                );

        // 판매자 알림 검증
        verify(notificationService, times(1))
                .sellerCreateNotification(
                        eq(sampleEvent.getSellerId()),  // sellerId 검증
                        eq(expectedSellerMessage),      // 메시지 내용 검증
                        eq("판매자 알림")
                );
    }

    @Test
    @DisplayName("배송 생성 실패 시 알림 발송 테스트")
    void consumeBidCompletionDeliveryFailureTest() {
        // Given
        when(deliveryService.createDelivery(any()))
                .thenThrow(new RuntimeException("배송 생성 실패"));

        // When & Then
        assertThatThrownBy(() ->
                bidCompletionConsumer.consumeBidCompletion(sampleEvent, 0, 0)
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("배송 생성 실패");

        // 배송 생성이 실패하면 알림도 발송되지 않아야 함
        verify(notificationService, never()).winnerCreateNotification(any(), any(), any());
        verify(notificationService, never()).sellerCreateNotification(any(), any(), any());
    }

    @Test
    @DisplayName("알림 서비스 실패 시 테스트")
    void notificationServiceFailureTest() {
        // Given
        when(deliveryService.createDelivery(any())).thenReturn(sampleDeliveryDto);
        doThrow(new RuntimeException("알림 발송 실패"))
                .when(notificationService)
                .winnerCreateNotification(any(), any(), any());

        // When & Then
        assertThatThrownBy(() ->
                bidCompletionConsumer.consumeBidCompletion(sampleEvent, 0, 0)
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("알림 발송 실패");

        // 배송은 생성되었지만 알림 발송은 실패
        verify(deliveryService, times(1)).createDelivery(any());
        verify(notificationService, times(1))
                .winnerCreateNotification(any(), any(), any());
        // 첫 번째 알림이 실패하면 두 번째 알림은 실행되지 않아야 함
        verify(notificationService, never())
                .sellerCreateNotification(any(), any(), any());
    }
}