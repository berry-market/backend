package com.berry.post.application.service.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductStatus;
import com.berry.post.domain.model.Review;
import com.berry.post.domain.repository.PostRepository;
import com.berry.post.domain.repository.ReviewRepository;
import com.berry.post.presentation.response.review.ReviewGradeResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

  @InjectMocks
  private ReviewServiceImpl reviewService;

  @Mock
  private PostRepository postRepository;

  @Mock
  private ReviewRepository reviewRepository;

  private Post post1;
  private Post post2;

  private Review review1;

  private Review review2;

  @BeforeEach
  void setUp() {
    post1 = Post.builder()
        .id(1L)
        .postCategoryId(1L)
        .writerId(1L)
        .productName("상품")
        .productContent("내용")
        .immediatePrice(50000)
        .startedPrice(20000)
        .auctionStartedAt(LocalDateTime.parse("2025-01-13T00:00:00"))
        .auctionEndedAt(LocalDateTime.parse("2025-03-03T00:00:00"))
        .productStatus(ProductStatus.ACTIVE)
        .deliveryMethod("배달")
        .deliveryFee(1000)
        .productImage("img_url")
        .likeCount(0)
        .viewCount(0)
        .bidPrice(null)
        .build();
    post2 = Post.builder()
        .id(2L)
        .postCategoryId(1L)
        .writerId(1L)
        .productName("상품")
        .productContent("내용")
        .immediatePrice(50000)
        .startedPrice(20000)
        .auctionStartedAt(LocalDateTime.parse("2025-01-13T00:00:00"))
        .auctionEndedAt(LocalDateTime.parse("2025-03-03T00:00:00"))
        .productStatus(ProductStatus.ACTIVE)
        .deliveryMethod("배달")
        .deliveryFee(1000)
        .productImage("img_url")
        .likeCount(0)
        .viewCount(0)
        .bidPrice(null)
        .build();

    review1 = Review.builder()
        .id(1L)
        .reviewerId(1L)
        .bidId(1L)
        .postId(1L)
        .reviewContent("리뷰 내용")
        .reviewScore(5)
        .build();

    review2 = Review.builder()
        .id(2L)
        .reviewerId(2L)
        .bidId(1L)
        .postId(2L)
        .reviewContent("리뷰 내용")
        .reviewScore(4)
        .build();
  }

  @Test
  @DisplayName("리뷰 평점 계산 성공 테스트")
  void getReviewGrade() {
    // given
    Long postId = 1L;
    Long writerId = 1L;

    List<Post> products = Arrays.asList(post1, post2);

    when(postRepository.findByIdAndDeletedYNFalse(postId)).thenReturn(Optional.of(post1));
    when(postRepository.findAllByWriterIdAndDeletedYNFalse(writerId)).thenReturn(products);
    when(reviewRepository.findByPostIdAndDeletedYNFalse(post1.getId())).thenReturn(review1);
    when(reviewRepository.findByPostIdAndDeletedYNFalse(post2.getId())).thenReturn(review2);

    // when
    ReviewGradeResponse response = reviewService.getReviewGrade(postId);

    // then
    assertThat(response).isNotNull();
    assertThat(response.getGrade()).isEqualTo(4.5);

  }
}