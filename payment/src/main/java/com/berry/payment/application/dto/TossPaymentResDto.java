package com.berry.payment.application.dto;

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

  public static TossPaymentResDto fromJson(JSONObject jsonResponse) {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    return TossPaymentResDto.builder()
        .paymentKey((String) jsonResponse.get("paymentKey"))
        .orderId((String) jsonResponse.get("orderId"))
        .orderName((String) jsonResponse.get("orderName"))
        .totalAmount(((Long) jsonResponse.get("totalAmount")).intValue())
        .status((String) jsonResponse.get("status"))
        .method((String) jsonResponse.get("method"))
        .requestedAt(parseDateTime((String) jsonResponse.get("requestedAt"), formatter))
        .approvedAt(parseDateTime((String) jsonResponse.get("approvedAt"), formatter))
        .build();
  }

  private static LocalDateTime parseDateTime(String dateTime, DateTimeFormatter formatter) {
    return dateTime != null ? LocalDateTime.parse(dateTime, formatter) : null;
  }
}