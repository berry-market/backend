package com.berry.payment.infrastructure.repository;

import com.berry.payment.application.dto.PaymentGetResDto;
import com.berry.payment.domain.model.Payment;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

  Page<PaymentGetResDto> findAllByBuyerIdAndRequestedAtBetween(
      Long buyerId,
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      Pageable pageable
  );

  Optional<Payment> findByPaymentKey(String paymentKey);
}