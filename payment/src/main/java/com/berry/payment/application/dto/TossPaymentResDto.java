package com.berry.payment.application.dto;

import com.berry.payment.domain.model.Payment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import org.json.simple.JSONObject;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TossPaymentResDto {

  private String paymentKey;
  private String orderId;
  private String orderName;
  private int totalAmount;
  private String status;
  private String method;
  private LocalDateTime requestedAt;
  private LocalDateTime approvedAt;

  public static TossPaymentResDto fromEntity(Payment payment) {
    return TossPaymentResDto.builder()
        .paymentKey(payment.getPaymentKey())
        .orderId(payment.getOrderId())
        .orderName(payment.getOrderName())
        .totalAmount(payment.getAmount())
        .status(payment.getPaymentStatus())
        .method(payment.getPaymentMethod())
        .requestedAt(payment.getRequestedAt())
        .approvedAt(payment.getApprovedAt())
        .build();
  }

  public static TossPaymentResDto fromJson(JSONObject confirmResponse) {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    return TossPaymentResDto.builder()
        .paymentKey((String) confirmResponse.get("paymentKey"))
        .orderId((String) confirmResponse.get("orderId"))
        .orderName((String) confirmResponse.get("orderName"))
        .totalAmount(((Long) confirmResponse.get("totalAmount")).intValue())
        .status((String) confirmResponse.get("status"))
        .method((String) confirmResponse.get("method"))
        .requestedAt(parseDateTime((String) confirmResponse.get("requestedAt"), formatter))
        .approvedAt(parseDateTime((String) confirmResponse.get("approvedAt"), formatter))
        .build();
  }

  private static LocalDateTime parseDateTime(String dateTime, DateTimeFormatter formatter) {
    return dateTime != null ? LocalDateTime.parse(dateTime, formatter) : null;
  }
}