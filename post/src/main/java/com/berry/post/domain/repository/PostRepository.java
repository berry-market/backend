package com.berry.post.domain.repository;

import com.berry.post.application.repository.PostRepositoryCustom;
import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

  List<Post> findAllByAuctionStartedAtBeforeAndProductStatusAndDeletedYNFalse(LocalDateTime now, ProductStatus productStatus);

  List<Post> findAllByAuctionEndedAtBeforeAndProductStatusNotAndDeletedYNFalse(LocalDateTime now, ProductStatus productStatus);

  Optional<Post> findByIdAndDeletedYNFalse(Long postId);

  List<Post> findAllByWriterIdAndDeletedYNFalse(Long writerId);
}
