package com.berry.payment.application.service;

import com.berry.payment.infrastructure.kafka.PaymentCompletedEvent;

public interface PaymentProducerService {

  void sendPaymentEvent(PaymentCompletedEvent event);

}
