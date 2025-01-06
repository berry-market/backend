package com.berry.post.domain.repository;

import com.berry.post.domain.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
  Page<Post> findAllAndDeletedYNFalse(String keyword, String type, Long postCategoryId, String sort, Pageable pageable);

}
