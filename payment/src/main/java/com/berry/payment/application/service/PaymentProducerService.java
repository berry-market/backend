package com.berry.payment.application.service;

import com.berry.payment.application.event.PaymentCompletedEvent;

public interface PaymentProducerService {

  void sendPaymentEvent(PaymentCompletedEvent event);

}
