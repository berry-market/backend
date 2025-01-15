package com.berry.post.application.repository;

import com.berry.post.domain.model.Post;
import com.berry.post.domain.model.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<Post> findAllAndDeletedYNFalse(String keyword, String type, Long postCategoryId, Long writerId, String sort, Pageable pageable, Long userId) {
    QPost post = QPost.post;

    BooleanBuilder builder = new BooleanBuilder();

    // 삭제 x
    builder.and(post.deletedYN.isFalse());

    // keyword 검색
    if (keyword != null && !keyword.isEmpty()) {
      builder.and(post.productName.containsIgnoreCase(keyword));
    }

    // type 에 따른 필터링 추가
    if (type != null && !type.isEmpty()) {
      if (type.equals("likes_count")) {
        builder.and(post.likeCount.goe(1)) // 찜 개수가 1 이상인 상품만 조회
                .and(post.auctionEndedAt.gt(LocalDateTime.now())); // 마감되지 않은 상품만 조회
      } else if (type.equals("auction_ended_at")) {
        builder.and(post.auctionEndedAt.gt(LocalDateTime.now())); // 마감되지 않은 상품만 조회
      }
    }

    // 카테고리 Id 필터링 추가
    if (postCategoryId != null) {
      builder.and(post.postCategoryId.eq(postCategoryId));
    }

    // 작성자 Id 필터링 추가
    if (writerId != null) {
      builder.and(post.writerId.eq(writerId));
    }

    // 정렬 기준 처리
    OrderSpecifier<?> orderSpecifier = getOrderSpecifier(post, sort, type);

    // 쿼리 실행
    List<Post> posts = jpaQueryFactory
        .selectFrom(post)
        .where(builder)
        .orderBy(orderSpecifier)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // 총 개수 조회 (페이징)
    long total = jpaQueryFactory
        .selectFrom(post)
        .where(builder)
        .fetchCount();

    return new PageImpl<>(posts, pageable, total);
  }

  private OrderSpecifier<?> getOrderSpecifier(QPost post, String sort, String type) {
    if (type != null && !type.isEmpty()) {
      if (type.equals("likes_count")) {
        return post.likeCount.desc();
      } else if (type.equals("auction_ended_at")) {
        return post.auctionEndedAt.asc();
      }
    }

    // 기본 정렬 처리
    return switch (sort) {
      case "latest" -> post.createdAt.desc(); // 최신 순
      case "old" -> post.createdAt.asc(); // 오래된 순
      case "auction_ended_at" -> post.auctionEndedAt.asc(); // 마감 임박 순
      case "view_count" -> post.viewCount.desc(); // 조회수 순
      case "likes_count" -> post.likeCount.desc(); // 찜하기 순
      default -> post.createdAt.desc(); // 기본값은 최신 순 정렬
    };
  }
}
