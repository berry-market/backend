package com.berry.post.domain.model;

import com.berry.common.auditor.BaseEntity;
import com.berry.post.presentation.request.Post.PostUpdateRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long postCategoryId;

  @Column(nullable = false)
  private Long writerId;

  @Column(nullable = false, length = 100)
  private String productName;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String productContent;

  @Column(nullable = false)
  private Integer immediatePrice;

  @Column(nullable = false)
  private Integer startedPrice;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  @Column(nullable = false)
  private LocalDateTime auctionStartedAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  @Column(nullable = false)
  private LocalDateTime auctionEndedAt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductStatus productStatus;

  @Column(nullable = false)
  private String deliveryMethod;

  @Column(nullable = false)
  private Integer deliveryFee;

  @Column(nullable = false)
  private String productImage;

  @Column(nullable = false)
  private Integer likeCount;

  @Column(nullable = false)
  private Integer viewCount;

  public void updateProductStatus(ProductStatus productStatus) {
    this.productStatus = productStatus;
  }

  public void updateViewCount() {
    this.viewCount++;
  }

  public void plusLikeCount() {
    this.likeCount++;
  }

  public void minusLikeCount() {
    this.likeCount--;
  }

  public void updateProduct(PostUpdateRequest postUpdateRequest) {
    if (postUpdateRequest.getPostCategoryId() != null) {
      this.postCategoryId = postUpdateRequest.getPostCategoryId();
    }
    if (postUpdateRequest.getProductName() != null) {
      this.productName = postUpdateRequest.getProductName();
    }
    if (postUpdateRequest.getProductContent() != null) {
      this.productContent = postUpdateRequest.getProductContent();
    }
    if (postUpdateRequest.getAuctionStartedAt() != null) {
      this.auctionStartedAt = postUpdateRequest.getAuctionStartedAt();
    }
    if (postUpdateRequest.getAuctionEndedAt() != null) {
      this.auctionEndedAt = postUpdateRequest.getAuctionEndedAt();
    }
    if (postUpdateRequest.getDeliveryMethod() != null) {
      this.deliveryMethod = postUpdateRequest.getDeliveryMethod();
    }
    if (postUpdateRequest.getDeliveryFee() != null) {
      this.deliveryFee = postUpdateRequest.getDeliveryFee();
    }
  }

  public void updateProductImage(String productImage) {
    if (productImage != null) {
      this.productImage = productImage;
    }
  }
}
