package com.berry.payment.application.dto;

import com.berry.payment.domain.model.Payment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;

public record TossPaymentResDto(
    String paymentKey,
    String orderId,
    String orderName,
    int totalAmount,
    String status,
    String method,
    LocalDateTime requestedAt,
    LocalDateTime approvedAt
) {
  public static TossPaymentResDto fromEntity(Payment payment) {
    return new TossPaymentResDto(
        payment.getPaymentKey(),
        payment.getOrderId(),
        payment.getOrderName(),
        payment.getAmount(),
        payment.getPaymentStatus(),
        payment.getPaymentMethod(),
        payment.getRequestedAt(),
        payment.getApprovedAt()
    );
  }

  public static TossPaymentResDto fromJson(JSONObject confirmResponse) {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    return new TossPaymentResDto(
        (String) confirmResponse.get("paymentKey"),
        (String) confirmResponse.get("orderId"),
        (String) confirmResponse.get("orderName"),
        ((Long) confirmResponse.get("totalAmount")).intValue(),
        (String) confirmResponse.get("status"),
        (String) confirmResponse.get("method"),
        parseDateTime((String) confirmResponse.get("requestedAt"), formatter),
        parseDateTime((String) confirmResponse.get("approvedAt"), formatter)
    );
  }

  private static LocalDateTime parseDateTime(String dateTime, DateTimeFormatter formatter) {
    return dateTime != null ? LocalDateTime.parse(dateTime, formatter) : null;
  }
}
