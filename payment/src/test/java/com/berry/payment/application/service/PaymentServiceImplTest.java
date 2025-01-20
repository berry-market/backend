package com.berry.payment.application.service;

import com.berry.common.role.Role;
import com.berry.payment.application.dto.*;
import com.berry.payment.domain.model.Payment;
import com.berry.payment.domain.repository.PaymentRepository;
import com.berry.payment.infrastructure.client.TossPaymentClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
class PaymentServiceImplTest {

  @InjectMocks
  private PaymentServiceImpl paymentService;

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private TossPaymentClient tossPaymentClient;

  @Mock
  private PaymentProducerService paymentProducer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    when(paymentRepository.getResponse(anyString())).thenReturn(null);
    doNothing().when(paymentProducer).sendCancelEvent(any());
  }

  @Test
  void saveTempPaymentData_ShouldSaveData() {
    // Given
    TempPaymentReqDto request = new TempPaymentReqDto("order123", 1000);
    doNothing().when(paymentRepository).saveTempPaymentData(anyString(), anyInt(), any());

    // When
    paymentService.saveTempPaymentData(request);

    // Then
    verify(paymentRepository, times(1)).saveTempPaymentData("order123", 1000, Duration.ofMinutes(10));
  }

  @Test
  void confirmPayment_ShouldReturnTossPaymentResDto_WhenValid() {
    // Given
    Long userId = 1L;
    ConfirmPaymentReqDto request = new ConfirmPaymentReqDto("order123", 1000, "paymentKey123");

    when(paymentRepository.findByPaymentKey(anyString())).thenReturn(Optional.empty());
    when(paymentRepository.getTempPaymentData(anyString())).thenReturn(Map.of("amount", 1000));

    TossPaymentResDto dummyResponse = new TossPaymentResDto(
        "paymentKey123", "order123", "Test Order", 1000, "APPROVED", "CARD",
        LocalDate.now().atStartOfDay(), LocalDate.now().atStartOfDay()
    );

    when(tossPaymentClient.confirmPayment(anyString(), anyString(), anyInt())).thenReturn(dummyResponse);
    doNothing().when(paymentRepository).deleteTempPaymentData(anyString());

    // When
    TossPaymentResDto response = paymentService.confirmPayment(userId, request);

    // Then
    assertNotNull(response);
    assertEquals("APPROVED", response.status());
    verify(paymentRepository, times(1)).save(any(Payment.class));
    verify(paymentProducer, times(1)).sendPaymentEvent(any());
  }

  @Test
  void getPayments_ShouldReturnPaymentPage_WhenValid() {
    // Given
    Long userId = 1L;
    Role role = Role.ADMIN;
    LocalDate startDate = LocalDate.now().minusDays(10);
    LocalDate endDate = LocalDate.now();
    PageRequest pageable = PageRequest.of(0, 10);

    PaymentGetResDto paymentRes = new PaymentGetResDto(
        1L, "paymentKey123", 1000, 500, "CARD", "APPROVED", LocalDate.now().atStartOfDay()
    );

    List<PaymentGetResDto> paymentList = Collections.singletonList(paymentRes);
    Page<PaymentGetResDto> dummyPage = new PageImpl<>(paymentList, pageable, 1);

    when(paymentRepository.findAllByBuyerIdAndRequestedAtBetween(
        anyLong(), any(), any(), eq(pageable)
    )).thenReturn(dummyPage);

    // When
    Page<PaymentGetResDto> result = paymentService.getPayments(userId, role, userId, startDate, endDate, pageable);

    // Then
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(paymentRepository, times(1)).findAllByBuyerIdAndRequestedAtBetween(
        anyLong(), any(), any(), eq(pageable)
    );
  }

  @Test
  void cancelPayment_ShouldUpdateCancelInfo_WhenValid() {
    // Given
    Long userId = 1L;
    Role role = Role.MEMBER;
    String paymentKey = "paymentKey123";
    String idempotencyKey = UUID.randomUUID().toString();

    TossCancelReqDto request = new TossCancelReqDto("user request", 500);

    Payment dummyPayment = Payment.builder()
        .paymentKey(paymentKey)
        .amount(1000)
        .balanceAmount(1000)
        .buyerId(userId)
        .build();

    TossCancelResDto dummyCancelResponse = new TossCancelResDto(
        "CANCELED", "cancel test", "transactionKey123", 500
    );

    when(paymentRepository.findByPaymentKey(paymentKey)).thenReturn(Optional.of(dummyPayment));
    when(tossPaymentClient.cancelPayment(paymentKey, request.cancelReason(), request.cancelAmount(), idempotencyKey))
        .thenReturn(dummyCancelResponse);

    // When
    paymentService.cancelPayment(userId, role, paymentKey, request, idempotencyKey);

    // Then
    assertPaymentUpdated(dummyPayment);
    verify(paymentProducer, times(1)).sendCancelEvent(any());
  }

  private void assertPaymentUpdated(Payment payment) {
    assertEquals("CANCELED", payment.getPaymentStatus());
    assertEquals(500, payment.getBalanceAmount());
    assertEquals("transactionKey123", payment.getTransactionKey());
  }
}
