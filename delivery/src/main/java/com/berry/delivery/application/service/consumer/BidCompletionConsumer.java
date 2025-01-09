package com.berry.delivery.application.service.consumer;

import com.berry.delivery.application.event.BidCompletionEvent;
import com.berry.delivery.application.service.delivery.DeliveryService;
import com.berry.delivery.presentation.dto.DeliveryDto;
import com.berry.delivery.presentation.dto.request.DeliveryCreateRequest;
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
}