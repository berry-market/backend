package com.berry.post.domain.repository;

import com.berry.post.domain.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
