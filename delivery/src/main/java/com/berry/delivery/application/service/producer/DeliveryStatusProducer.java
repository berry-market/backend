package com.berry.delivery.application.service.producer;

import com.berry.delivery.application.event.DeliveryStatusEvent;
import com.berry.delivery.domain.model.delivery.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.KafkaException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryStatusProducer {
    private final KafkaTemplate<String, DeliveryStatusEvent> kafkaTemplate;

    @Value("${kafka.topic.delivery-status}")
    private String topicName;

    public void publishDeliveryStatus(Long bidId, DeliveryStatus status) {
        DeliveryStatusEvent event = new DeliveryStatusEvent(bidId, status);

        try {
            kafkaTemplate.send(topicName, String.valueOf(bidId), event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("배송 상태 이벤트 발행 성공. bidId={}, status={}", bidId, status);
                        } else {
                            log.error("배송 상태 이벤트 발행 실패. bidId={}, status={}", bidId, status, ex);
                        }
                    });
        } catch (Exception e) {
            log.error("배송 상태 이벤트 발행 중 에러 발생. bidId={}, status={}", bidId, status, e);
            throw new KafkaException("배송 상태 이벤트 발행 실패: " + e.getMessage(), e);
        }
    }
}