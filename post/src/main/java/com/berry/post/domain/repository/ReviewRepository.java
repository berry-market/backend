package com.berry.post.domain.repository;

import com.berry.post.domain.model.Review;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long > {

  Optional<Review> findByReviewerIdAndPostIdAndDeletedYNFalse(Long reviewerId, Long postId);
}
