package com.berry.delivery.application.service.consumer;

import com.berry.delivery.application.event.BidCompletionEvent;
import com.berry.delivery.application.service.delivery.DeliveryService;
import com.berry.delivery.application.service.notification.NotificationService;
import com.berry.delivery.presentation.dto.DeliveryDto;
import com.berry.delivery.presentation.dto.request.delivery.DeliveryCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BidCompletionConsumer {
    private final DeliveryService deliveryService;
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${kafka.topic.bid-completion}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeBidCompletion(
            @Payload BidCompletionEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Bid 낙찰 완료 이벤트 수신. bidId={}, partition={}, offset={}",
                event.getBidId(), partition, offset);

        processBidCompletion(event);
        sendNotifications(event);
    }

    private void processBidCompletion(BidCompletionEvent event) {
        try {
            DeliveryCreateRequest request = new DeliveryCreateRequest(
                    null,
                    event.getWinnerId(),   // 낙찰자를 수령인으로
                    event.getSellerId(),   // 판매자를 발송인으로
                    event.getBidId()
            );

            DeliveryDto delivery = deliveryService.createDelivery(request);
            log.info("배송 생성 완료. deliveryId={}, bidId={}",
                    delivery.deliveryId(), event.getBidId());

        } catch (Exception e) {
            log.error("배송 생성 실패. bidId={}", event.getBidId(), e);
            throw e;
        }
    }
    private void sendNotifications(BidCompletionEvent event) {
        winnerCreateNotification(event);
        sellerCreateNotification(event);
    }

    private void winnerCreateNotification(BidCompletionEvent event) {
        try {
            String winnerMessage = String.format(
                    "상품 낙찰자로 선정되었습니다!"
            );

            NotificationCreateRequest request = new NotificationCreateRequest(
                    null,
                    event.getWinnerId(),
                    null,
                    winnerMessage
            );

            notificationService.createNotification(request);
            log.info("낙찰자 알림 발송 완료. winnerId={}, bidId={}",
                    event.getWinnerId(), event.getBidId());

        } catch (Exception e) {
            log.error("낙찰자 알림 발송 실패. winnerId={}, bidId={}",
                    event.getWinnerId(), event.getBidId(), e);
            // 알림 실패는 치명적이지 않으므로 예외를 던지지 않음
        }
    }

    private void sellerCreateNotification(BidCompletionEvent event) {
        try {
            String sellerMessage = String.format(
                    "등록하신 상품이 낙찰되었습니다."
            );

            NotificationCreateRequest request = new NotificationCreateRequest(
                    null,
                    event.getSellerId(),
                    null,
                    sellerMessage
            );

            notificationService.createNotification(request);
            log.info("판매자 알림 발송 완료. sellerId={}, bidId={}",
                    event.getSellerId(), event.getBidId());

        } catch (Exception e) {
            log.error("판매자 알림 발송 실패. sellerId={}, bidId={}",
                    event.getSellerId(), event.getBidId(), e);
        }
    }
}
