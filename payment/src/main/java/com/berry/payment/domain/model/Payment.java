package com.berry.payment.domain.model;

import com.berry.common.auditor.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment")
@SQLRestriction(value = "deleted_yn = false")
public class Payment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long buyerId;

  @Column(nullable = false, unique = true, length = 200)
  private String paymentKey;

  @Column(nullable = false, unique = true, length = 100)
  private String orderId;

  @Column(nullable = false)
  private String orderName;

  @Column(nullable = false)
  private int amount;

  @Column(nullable = false)
  private String paymentMethod;

  @Column(nullable = false)
  private String paymentStatus;

  @Column
  private String transactionKey;

  @Column(nullable = false)
  private int balanceAmount;

  @Column
  private String cancelReason;

  @Column(nullable = false)
  private LocalDateTime requestedAt;

  @Column
  private LocalDateTime approvedAt;


  public void updateCancelInfo(String status, String reason, int balanceAmount,
      String transactionKey) {
    if (this.transactionKey != null && this.transactionKey.equals(transactionKey)) {
      return;
    }
    this.paymentStatus = status;
    this.cancelReason = reason;
    this.balanceAmount = balanceAmount;
    this.transactionKey = transactionKey;
  }
}