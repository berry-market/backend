package com.berry.post.domain.repository;

import com.berry.post.domain.model.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUserId(Long userId);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
}
