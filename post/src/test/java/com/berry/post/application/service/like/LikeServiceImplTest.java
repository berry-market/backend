package com.berry.post.application.service.like;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.post.domain.model.Like;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductStatus;
import com.berry.post.domain.repository.LikeRepository;
import com.berry.post.domain.repository.PostRepository;
import com.berry.post.presentation.request.like.CreatePostLikeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {

    @InjectMocks
    private LikeServiceImpl likeService;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("유효한 요청일 때 찜이 저장되어야 한다")
    public void createPostLike_ShouldSaveLike_WhenRequestIsValid() {
        // given
        CreatePostLikeRequest request = new CreatePostLikeRequest(1L);
        Long userId = 1L;

        Post post = Post.builder()
            .id(1L)
            .postCategoryId(1L)
            .writerId(1L)
            .productName("Sample Product")
            .productContent("Sample Content")
            .immediatePrice(1000)
            .startedPrice(500)
            .auctionStartedAt(LocalDateTime.now())
            .auctionEndedAt(LocalDateTime.now().plusDays(1))
            .productStatus(ProductStatus.PENDING)
            .deliveryMethod("Courier")
            .deliveryFee(50)
            .productImage("sample.jpg")
            .likeCount(0)
            .viewCount(0)
            .bidPrice(null)
            .build();

        given(postRepository.findById(request.postId())).willReturn(Optional.of(post));

        // when
        likeService.createPostLike(request, userId);

        // then
        ArgumentCaptor<Like> likeCaptor = ArgumentCaptor.forClass(Like.class);
        then(likeRepository).should().save(likeCaptor.capture());

        Like savedLike = likeCaptor.getValue();
        assertEquals(userId, savedLike.getUserId());
        assertEquals(request.postId(), savedLike.getPostId());
    }

    @Test
    @DisplayName("게시글 ID가 올바르지 않을 때 예외가 발생해야 한다")
    public void createPostLike_ShouldThrowException_WhenUserIdsDoNotMatch() {
        // given
        CreatePostLikeRequest request = new CreatePostLikeRequest(100L);
        Long userId = 1L;

        given(postRepository.findById(request.postId())).willReturn(Optional.empty()); // 게시글을 찾을 수 없도록 설정

        // when & then
        assertThrows(CustomApiException.class, () -> likeService.createPostLike(request, userId));
    }
}
