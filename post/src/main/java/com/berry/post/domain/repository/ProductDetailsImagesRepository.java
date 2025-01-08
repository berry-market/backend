package com.berry.post.domain.repository;

import com.berry.post.domain.model.ProductDetailsImages;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailsImagesRepository extends JpaRepository<ProductDetailsImages, Long> {

  List<ProductDetailsImages> findAllByPostIdAndDeletedYNFalseOrderBySequenceAsc(Long postId);
}
