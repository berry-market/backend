package com.berry.post.presentation.response.Post;

import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostServerResponse {

  private Long postCategoryId;
  private Long writerId;
  private String productName;
  private String productContent;
  private Integer immediatePrice;
  private Integer startedPrice;
  private LocalDateTime auctionStartedAt;
  private LocalDateTime auctionEndedAt;
  private ProductStatus productStatus;
  private String deliveryMethod;
  private Integer deliveryFee;
  private String productImage;
  private Integer likeCount;
  private Integer viewCount;

  public PostServerResponse(Post post) {
    this.postCategoryId = post.getPostCategoryId();
    this.writerId = post.getWriterId();
    this.productName = post.getProductName();
    this.productContent = post.getProductContent();
    this.immediatePrice = post.getImmediatePrice();
    this.startedPrice = post.getStartedPrice();
    this.auctionStartedAt = post.getAuctionStartedAt();
    this.auctionEndedAt = post.getAuctionEndedAt();
    this.productStatus = post.getProductStatus();
    this.deliveryMethod = post.getDeliveryMethod();
    this.deliveryFee = post.getDeliveryFee();
    this.productImage = post.getProductImage();
    this.likeCount = post.getLikeCount();
    this.viewCount = post.getViewCount();
  }
}
