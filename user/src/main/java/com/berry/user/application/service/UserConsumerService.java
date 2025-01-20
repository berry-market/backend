package com.berry.user.application.service;

import com.berry.common.response.ResErrorCode;
import com.berry.user.application.model.event.UserEvent;
import com.berry.user.domain.model.User;
import com.berry.user.domain.repository.UserJpaRepository;
import com.berry.user.infrastructure.model.PaymentEvent;
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

    @KafkaListener(topics = "payment-completed", groupId = "user-service-group", containerFactory = "paymentCompletedEventKafkaListenerContainerFactory")
    @Transactional
    public void completePayment(PaymentEvent event) {
        log.info("결제 완료 이벤트 수신. userId={}, amount={}", event.getUserId(), event.getAmount());
        User user = getUser(event.getUserId());

        user.updatePoint(event.getUserId(), event.getAmount());
    }

    @KafkaListener(topics = "payment-canceled", groupId = "user-service-group", containerFactory = "paymentCanceledEventKafkaListenerContainerFactory")
    @Transactional
    public void cancelPayment(PaymentEvent event) { // TODO : 이전 충전 기록이 있는지 확인 필요
        log.info("결제 취소 이벤트 수신. userId={}, amount={}", event.getUserId(), event.getAmount());
        User user = getUser(event.getUserId());

        user.updatePoint(event.getUserId(), event.getAmount());
    }

    @KafkaListener(topics = "user-events", groupId = "user-service-group", containerFactory = "userEventBiddingKafkaListenerContainerFactory")
    @Transactional
    public void updatePoints(UserEvent.Bidding event) {
        log.info("입찰 완료 이벤트 수신. userId={}, amount={}", event.getUserId(), event.getAmount());
        User user = getUser(event.getUserId());

        user.updatePoint(event.getUserId(), event.getAmount());
    }

    private User getUser(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(ResErrorCode.NOT_FOUND.getMessage()));
    }

}
