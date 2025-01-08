package com.berry.post.presentation.request.Post;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostUpdateRequest {

  private Long postCategoryId;

  @Size(max = 100, message = "100 글자 이내로 작성해주세요.")
  private String productName;

  private String productContent;

  @Future(message = "미래 날짜만 선택 가능합니다.")
  private LocalDateTime auctionStartedAt;

  @Future(message = "미래 날짜만 선택 가능합니다.")
  private LocalDateTime auctionEndedAt;

  private String deliveryMethod;

  private Integer deliveryFee;

}
