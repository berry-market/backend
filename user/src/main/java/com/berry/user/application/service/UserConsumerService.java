package com.berry.user.application.service;

import com.berry.common.response.ResErrorCode;
import com.berry.user.application.model.event.UserEvent;
import com.berry.user.domain.model.User;
import com.berry.user.domain.repository.UserJpaRepository;
import com.berry.user.infrastructure.model.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserConsumerService {

    private final UserJpaRepository userJpaRepository;

    @KafkaListener(topics = "payment-completed", groupId = "user-service-group")
    @Transactional
    public void completePayment(PaymentCompletedEvent event) {
        log.info("결제 완료 이벤트 수신. userId={}, amount={}", event.getUserId(), event.getAmount());

        User user = userJpaRepository.findById(event.getUserId())
            .orElseThrow(() -> new IllegalArgumentException(ResErrorCode.NOT_FOUND.getMessage()));

        user.chargePoint(event.getUserId(), event.getAmount());
    }

    @KafkaListener(topics = "user-events", groupId = "user-service-group")
    public void updatePoints(UserEvent.Bidding event) {
        log.info("입찰 완료 이벤트 수신. userId={}, amount={}", event.getUserId(), event.getAmount());

        User user = userJpaRepository.findById(event.getUserId())
            .orElseThrow(() -> new IllegalArgumentException(ResErrorCode.NOT_FOUND.getMessage()));

        user.chargePoint(event.getUserId(), event.getAmount());
    }

}
