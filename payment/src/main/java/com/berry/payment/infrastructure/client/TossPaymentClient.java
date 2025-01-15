package com.berry.payment.infrastructure.client;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.payment.application.dto.TossCancelResDto;
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

  private static final String TOSS_API_URL = "https://api.tosspayments.com/v1/payments";
  private final Logger logger = LoggerFactory.getLogger(TossPaymentClient.class);

  @Value("${TOSS_SECRET_KEY}")
  private String secretKey;

  public TossPaymentResDto confirmPayment(String orderId, String paymentKey, int amount) {
    try {
      String url = TOSS_API_URL + "/confirm";

      JSONObject requestPayload = new JSONObject();
      requestPayload.put("orderId", orderId);
      requestPayload.put("amount", amount);
      requestPayload.put("paymentKey", paymentKey);

      JSONObject confirmResponse = sendPostRequest(url, requestPayload, null);

      return TossPaymentResDto.fromJson(confirmResponse);

    } catch (Exception e) {
      logger.error("토스 결제 승인 API 호출 중 오류 발생", e);
      throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
          "결제 승인 요청 중 오류가 발생했습니다.: " + e.getMessage());
    }
  }

  public TossCancelResDto cancelPayment(String paymentKey, String cancelReason,
      Integer cancelAmount,
      String idempotencyKey) {
    try {
      String url = TOSS_API_URL + "/" + paymentKey + "/cancel";
      JSONObject requestPayload = new JSONObject();
      requestPayload.put("cancelReason", cancelReason);
      if (cancelAmount != null) {
        requestPayload.put("cancelAmount", cancelAmount);
      }

      JSONObject cancelResponse = sendPostRequest(url, requestPayload, idempotencyKey);

      return TossCancelResDto.fromJson(cancelResponse);

    } catch (Exception e) {
      logger.error("토스 결제 취소 API 호출 중 오류 발생", e);
      throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
          "결제 취소 요청 중 오류가 발생했습니다.: " + e.getMessage());
    }
  }

  private JSONObject sendPostRequest(String url, JSONObject payload, String idempotencyKey) {
    HttpURLConnection connection = null;
    try {
      connection = createConnection(url, idempotencyKey);
      sendRequestPayload(connection, payload);
      return handleResponse(connection);

    } catch (Exception e) {
      logger.error("HTTP 요청 처리 중 오류 발생", e);
      throw new CustomApiException(ResErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  private HttpURLConnection createConnection(String url, String idempotencyKey) throws Exception {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    String authHeader = createAuthorizationHeader();

    connection.setRequestProperty("Authorization", authHeader);
    connection.setRequestProperty("Content-Type", "application/json");
    if (idempotencyKey != null) {
      connection.setRequestProperty("Idempotency-Key", idempotencyKey);
    }
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);

    return connection;
  }

  private void sendRequestPayload(HttpURLConnection connection, JSONObject payload)
      throws Exception {
    try (OutputStream os = connection.getOutputStream()) {
      os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
    }
  }

  private JSONObject handleResponse(HttpURLConnection connection) throws Exception {
    int responseCode = connection.getResponseCode();
    InputStream responseStream =
        responseCode == 200 ? connection.getInputStream() : connection.getErrorStream();

    JSONParser parser = new JSONParser();
    JSONObject jsonResponse = (JSONObject) parser.parse(
        new InputStreamReader(responseStream, StandardCharsets.UTF_8));

    if (responseCode != 200) {
      throw new CustomApiException(ResErrorCode.API_CALL_FAILED,
          "응답 코드: " + responseCode + ", 메시지: " + jsonResponse.toJSONString());
    }

    return jsonResponse;
  }

  private String createAuthorizationHeader() {
    return "Basic " + Base64.getEncoder()
        .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
  }
}