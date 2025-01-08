package com.berry.post.domain.repository;

import com.berry.post.domain.model.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long > {

  Optional<Review> findByReviewerIdAndPostIdAndDeletedYNFalse(Long reviewerId, Long postId);

  Page<Review> findAllByDeletedYNFalse(Pageable pageable);

  Page<Review> findAllByReviewContentAndDeletedYNFalse(Pageable pageable, String keyword);

  Review findByPostIdAndDeletedYNFalse(Long postId);

  List<Review> findAllByPostIdAndDeletedYNFalse(Long postId);
}
