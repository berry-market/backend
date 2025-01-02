package com.berry.post.domain.repository;

import com.berry.post.domain.model.PostCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

  Optional<PostCategory> findByCategoryNameAndDeletedYNFalse(String categoryName);
}
