package com.berry.payment.application.service;

import com.berry.payment.application.event.PaymentEvent;

public interface PaymentProducerService {

  void sendPaymentEvent(PaymentEvent event);

  void sendCancelEvent(PaymentEvent event);
}
