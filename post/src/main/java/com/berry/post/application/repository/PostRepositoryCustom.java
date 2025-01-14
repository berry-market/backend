package com.berry.post.application.repository;

import com.berry.post.domain.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
  Page<Post> findAllAndDeletedYNFalse(String keyword, String type, Long postCategoryId, Long writerId, String sort, Pageable pageable, Long userId);

}