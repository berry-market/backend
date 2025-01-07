package com.berry.post.presentation.request.Post;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostCreateRequest {

  @NotNull(message = "게시글 카테고리 선택은 필수입니다.")
  private Long postCategoryId;

  @NotNull
  private Long writerId;

  @NotBlank(message = "상품 이름은 필수입니다.")
  @Size(max = 100, message = "100 글자 이내로 작성해주세요.")
  private String productName;

  @NotBlank(message = "상품 설명은 필수입니다.")
  private String productContent;

  @NotNull(message = "즉시 구매 가격은 필수입니다.")
  private Integer immediatePrice;

  @NotNull(message = "경매 시작 가격은 필수입니다.")
  private Integer startedPrice;

  @NotNull(message = "경매 시작 날짜는 필수입니다.")
  @Future(message = "미래 날짜만 선택 가능합니다.")
  private LocalDateTime auctionStartedAt;

  @NotNull(message = "경매 종료 날짜는 필수입니다.")
  @Future(message = "미래 날짜만 선택 가능합니다.")
  private LocalDateTime auctionEndedAt;

  @NotBlank(message = "배달 방법 선택은 필수입니다.")
  private String deliveryMethod;

  @NotNull(message = "배달비 선택은 필수입니다.")
  private Integer deliveryFee;
}
