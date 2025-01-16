package com.berry.payment.infrastructure.service;

import com.berry.payment.application.service.PaymentProducerService;
import com.berry.payment.application.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProducerServiceImpl implements PaymentProducerService {

  private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

  @Override
  public void sendPaymentEvent(PaymentEvent event) {
    kafkaTemplate.send("payment-completed", event);
    System.out.println("Payment event sent to Kafka: " + event);
  }

  @Override
  public void sendCancelEvent(PaymentEvent event) {
    kafkaTemplate.send("payment-canceled", event);
    System.out.println("Cancel event sent to Kafka: " + event);
  }
}