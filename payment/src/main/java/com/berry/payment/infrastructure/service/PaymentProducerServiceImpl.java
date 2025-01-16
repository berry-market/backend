package com.berry.payment.infrastructure.service;

import com.berry.payment.application.service.PaymentProducerService;
import com.berry.payment.application.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProducerServiceImpl implements PaymentProducerService {

  private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

  public void sendPaymentEvent(PaymentCompletedEvent event) {
    kafkaTemplate.send("payment-completed", event);
    System.out.println("Payment event sent to Kafka: " + event);
  }
}