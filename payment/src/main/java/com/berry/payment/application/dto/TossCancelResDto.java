package com.berry.payment.application.dto;

import lombok.Builder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Builder
public record TossCancelResDto(String status, String cancelReason, String transactionKey, int balanceAmount) {

  public static TossCancelResDto fromJson(JSONObject cancelResponse) {
    JSONArray cancelsArray = (JSONArray) cancelResponse.get("cancels");

    if (cancelsArray == null || cancelsArray.isEmpty()) {
      // TODO: /v1/payments/{paymentKey} 토스에 조회
    }

    JSONObject lastCancelInfo = (JSONObject) cancelsArray.get(cancelsArray.size() - 1);

    return TossCancelResDto.builder()
        .status((String) cancelResponse.get("status"))
        .cancelReason((String) lastCancelInfo.get("cancelReason"))
        .transactionKey((String) lastCancelInfo.get("transactionKey"))
        .balanceAmount(((Long) cancelResponse.get("balanceAmount")).intValue())
        .build();
  }
}
