package com.berry.post.presentation.response.Post;

import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponse {
  private Long categoryId;
  private String productName;
  private Integer immediatePrice;
  private LocalDateTime auctionStartedAt;
  private LocalDateTime auctionEndedAt;
  private ProductStatus productStatus;
  private String productImage;
  private Integer viewCount;
  private Boolean isLiked;

  public PostResponse(Post post, Boolean isLiked) {
    this.categoryId = post.getPostCategoryId();
    this.productName = post.getProductName();
    this.immediatePrice = post.getImmediatePrice();
    this.auctionStartedAt = post.getAuctionStartedAt();
    this.auctionEndedAt = post.getAuctionEndedAt();
    this.productStatus = post.getProductStatus();
    this.productImage = post.getProductImage();
    this.viewCount = post.getViewCount();
    this.isLiked = isLiked;
  }
}
