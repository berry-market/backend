package com.berry.post.domain.repository;

import com.berry.post.domain.model.PostCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

  Optional<PostCategory> findByCategoryNameAndDeletedYNFalse(String categoryName);

  List<PostCategory> findAllByDeletedYNFalse();

  Page<PostCategory> findAllByDeletedYNFalse(Pageable pageable);

  Page<PostCategory> findAllByCategoryNameAndDeletedYNFalse(Pageable pageable, String categoryName);

  Optional<PostCategory> findByIdAndDeletedYNFalse(Long categoryId);
}
