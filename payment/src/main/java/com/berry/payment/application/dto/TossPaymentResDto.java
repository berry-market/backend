package com.berry.payment.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
  private String requestedAt;
  private String approvedAt;

  public static TossPaymentResDto fromJson(JSONObject jsonResponse) {
    return TossPaymentResDto.builder()
        .paymentKey((String) jsonResponse.get("paymentKey"))
        .orderId((String) jsonResponse.get("orderId"))
        .orderName((String) jsonResponse.get("orderName"))
        .totalAmount(((Long) jsonResponse.get("totalAmount")).intValue())
        .status((String) jsonResponse.get("status"))
        .method((String) jsonResponse.get("method"))
        .requestedAt((String) jsonResponse.get("requestedAt"))
        .approvedAt((String) jsonResponse.get("approvedAt"))
        .build();
  }
}