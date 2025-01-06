package com.berry.post.presentation.response.Post;

import com.berry.post.domain.model.Post;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class PostDetailsResponse {

  private Long postCategoryId;
  private String productName;
  private Integer immediatePrice;
  private LocalDateTime auctionStartedAt;
  private LocalDateTime auctionEndedAt;
  private Integer startedPrice;
  private String deliveryMethod;
  private Integer deliveryFee;
  private String productImage;
  private String productContent;
  private Integer likeCount;
  private Integer viewCount;

  private List<String> productDetailsImages;
  private Boolean isLiked;
  private String writerNickname;
  private Integer bidPrice;

  public PostDetailsResponse(Post post, List<String> productDetailsImages, Boolean isLiked, String writerNickName, Integer bidPrice) {
    this.postCategoryId = post.getPostCategoryId();
    this.productName = post.getProductName();
    this.immediatePrice = post.getImmediatePrice();
    this.auctionStartedAt = post.getAuctionStartedAt();
    this.auctionEndedAt = post.getAuctionEndedAt();
    this.startedPrice = post.getStartedPrice();
    this.deliveryMethod = post.getDeliveryMethod();
    this.deliveryFee = post.getDeliveryFee();
    this.productImage = post.getProductImage();
    this.productContent = post.getProductContent();
    this.likeCount = post.getLikeCount();
    this.viewCount = post.getViewCount();

    this.productDetailsImages = productDetailsImages;
    this.isLiked = isLiked;
    this.writerNickname = writerNickName;
    this.bidPrice = bidPrice;
  }
}
