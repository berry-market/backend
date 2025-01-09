package com.berry.post.application.service.consumer;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.post.application.model.event.PostEvent;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.repository.PostRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Getter
@Service
@RequiredArgsConstructor
public class PostConsumerServiceImpl implements PostConsumerService{

  private final PostRepository postRepository;

  @Override
  @KafkaListener(topics = "post-events")
  public void receiveBidEvent(PostEvent.Price event) {
    Post post = postRepository.findByIdAndDeletedYNFalse(event.getPostId()).orElseThrow(
        () -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
    );

    Integer successfulBidPrice = event.getSuccessfulBidPrice();
    if (successfulBidPrice != null) {
      post.updateBidPrice(successfulBidPrice);
      postRepository.save(post);
    }
  }
}
