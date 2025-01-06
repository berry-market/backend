package com.berry.post.presentation.response.Post;

import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostListResponse {
  private Long postCategoryId;
  private String productName;
  private Integer immediatePrice;
  private LocalDateTime auctionStartedAt;
  private LocalDateTime auctionEndedAt;
  private ProductStatus productStatus;
  private String productImage;
  private Integer viewCount;
  private Boolean isLiked;

  public PostListResponse(Post post, Boolean isLiked) {
    this.postCategoryId = post.getPostCategoryId();
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
