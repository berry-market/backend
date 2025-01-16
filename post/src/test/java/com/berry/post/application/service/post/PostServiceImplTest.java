package com.berry.post.application.service.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.berry.post.domain.model.Like;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductStatus;
import com.berry.post.domain.repository.LikeRepository;
import com.berry.post.domain.repository.PostRepository;
import com.berry.post.presentation.response.Post.PostListResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

  @InjectMocks
  private PostServiceImpl postService;

  @Mock
  private PostRepository postRepository;

  @Mock
  private LikeRepository likeRepository;

  @Mock
  private CacheManager cacheManager;

  @Mock
  private Cache cache;

  private Post post1;
  private Post post2;
  private Pageable pageable;

  @BeforeEach
  void setUp() {
    post1 = Post.builder()
        .id(1L)
        .postCategoryId(1L)
        .writerId(2L)
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
        .writerId(2L)
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
    pageable = PageRequest.of(0, 5);
  }


  @Test
  @DisplayName("게시글 전체 조회 성공 테스트")
  void getPosts() {
    // given
    Long userId = 1L;
    Page<Post> postsPage = new PageImpl<>(List.of(post1, post2), pageable, 2);

    when(postRepository.findAllAndDeletedYNFalse(anyString(), anyString(), anyLong(), anyLong(), anyString(), any(Pageable.class), anyLong()))
        .thenReturn(postsPage);
    when(likeRepository.findByUserIdAndPostId(eq(userId), eq(1L))).thenReturn(Optional.of(
        Like.builder()
            .id(1L)
            .userId(1L)
            .postId(1L)
            .createdAt(LocalDateTime.now())
            .build())); // userId = 1 인 유저는 postId = 1에 좋아요를 한 상태

    // when
    Page<PostListResponse> result = postService.getPosts("상품", "", 1L, 2L, "", pageable, userId);

    // then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());

    PostListResponse response1 = result.getContent().get(0);
    PostListResponse response2 = result.getContent().get(1);

    assertTrue(response1.getIsLiked());
    assertFalse(response2.getIsLiked());
  }
}