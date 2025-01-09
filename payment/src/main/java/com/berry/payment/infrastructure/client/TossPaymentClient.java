package com.berry.payment.infrastructure.client;

import com.berry.payment.application.dto.TossPaymentResDto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class TossPaymentClient {

  private static final String TOSS_API_URL = "https://api.tosspayments.com/v1/payments/confirm";
  private final Logger logger = LoggerFactory.getLogger(TossPaymentClient.class);

  @Value("${TOSS_SECRET_KEY}")
  private String secretKey;

  public TossPaymentResDto confirmPayment(String orderId, String paymentKey, int amount) {
    try {
      // 요청 JSON 생성
      JSONObject obj = new JSONObject();
      obj.put("orderId", orderId);
      obj.put("amount", amount);
      obj.put("paymentKey", paymentKey);

      // Authorization 헤더 생성
      Base64.Encoder encoder = Base64.getEncoder();
      String authHeader =
          "Basic " + new String(encoder.encode((secretKey + ":").getBytes(StandardCharsets.UTF_8)),
              StandardCharsets.UTF_8);

      // HTTP 연결 설정
      URL url = new URL(TOSS_API_URL);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestProperty("Authorization", authHeader);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);

      // 요청 데이터 전송
      try (OutputStream os = connection.getOutputStream()) {
        os.write(obj.toString().getBytes(StandardCharsets.UTF_8));
      }

      // 응답 처리
      int responseCode = connection.getResponseCode();
      InputStream responseStream =
          responseCode == 200 ? connection.getInputStream() : connection.getErrorStream();

      JSONParser parser = new JSONParser();
      JSONObject jsonResponse = (JSONObject) parser.parse(
          new InputStreamReader(responseStream, StandardCharsets.UTF_8));
      responseStream.close();

      if (responseCode != 200) {
        throw new RuntimeException(
            "결제 승인 실패 - 응답 코드: " + responseCode + ", 메시지: " + jsonResponse.toJSONString());
      }

      // 응답 매핑
      return TossPaymentResDto.fromJson(jsonResponse);

    } catch (Exception e) {
      logger.error("토스 결제 API 호출 중 오류 발생", e);
      throw new RuntimeException("토스 결제 API 호출 실패", e);
    }
  }
}