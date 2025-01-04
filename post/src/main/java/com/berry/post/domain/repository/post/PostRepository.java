package com.berry.post.domain.repository.post;

import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.ProductStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

  List<Post> findAllByAuctionStartedAtBeforeAndProductStatusNotAndDeletedYNFalse(LocalDateTime now, ProductStatus productStatus);

  List<Post> findAllByAuctionEndedAtBeforeAndProductStatusNotAndDeletedYNFalse(LocalDateTime now, ProductStatus productStatus);
}
