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

  @Column
  private Integer bidPrice;

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
    updatePostCategoryId(postUpdateRequest.getPostCategoryId());
    updateProductName(postUpdateRequest.getProductName());
    updateProductContent(postUpdateRequest.getProductContent());
    updateAuctionStartedAt(postUpdateRequest.getAuctionStartedAt());
    updateAuctionEndedAt(postUpdateRequest.getAuctionEndedAt());
    updateDeliveryMethod(postUpdateRequest.getDeliveryMethod());
    updateDeliveryFee(postUpdateRequest.getDeliveryFee());
  }

  private void updatePostCategoryId(Long postCategoryId) {
    if (postCategoryId != null) {
      this.postCategoryId = postCategoryId;
    }
  }

  private void updateProductName(String productName) {
    if (productName != null) {
      this.productName = productName;
    }
  }

  private void updateProductContent(String productContent) {
    if (productContent != null) {
      this.productContent = productContent;
    }
  }

  private void updateAuctionStartedAt(LocalDateTime auctionStartedAt) {
    if (auctionStartedAt != null) {
      this.auctionStartedAt = auctionStartedAt;
    }
  }

  private void updateAuctionEndedAt(LocalDateTime auctionEndedAt) {
    if (auctionEndedAt != null) {
      this.auctionEndedAt = auctionEndedAt;
    }
  }

  private void updateDeliveryMethod(String deliveryMethod) {
    if (deliveryMethod != null) {
      this.deliveryMethod = deliveryMethod;
    }
  }

  private void updateDeliveryFee(Integer deliveryFee) {
    if (deliveryFee != null) {
      this.deliveryFee = deliveryFee;
    }
  }

  public void updateProductImage(String productImage) {
    if (productImage != null) {
      this.productImage = productImage;
    }
  }

  public void updateBidPrice(Integer successfulBidPrice) {
    this.bidPrice = successfulBidPrice;
  }
}
