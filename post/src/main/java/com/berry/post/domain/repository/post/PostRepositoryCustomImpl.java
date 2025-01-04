package com.berry.post.domain.repository.post;

import com.berry.post.domain.model.Post;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
  public Page<Post> findAllAndDeletedYNFalse(String keyword, String type, Long postCategoryId, Pageable pageable) {
    QPost post = QPost.post;

    // 조건 빌더
    BooleanBuilder builder = new BooleanBuilder();

    // 삭제되지 않은 게시글만 조회
    builder.and(post.deletedYN.isFalse());

    // 1. keyword 검색 조건 (제목, 내용)
    if (keyword != null && !keyword.isEmpty()) {
      builder.and(post.title.containsIgnoreCase(keyword)
          .or(post.content.containsIgnoreCase(keyword)));
    }

    // 2. 카테고리 ID로 필터링
    if (postCategoryId != null) {
      builder.and(post.category.id.eq(postCategoryId));  // 카테고리 ID 필터링
    }

    // 3. 정렬 기준 처리 (type에 따라 다르게 처리)
    OrderSpecifier<?> orderSpecifier = getOrderSpecifier(post, type);

    // 쿼리 실행
    List<Post> posts = jpaQueryFactory
        .selectFrom(post)
        .where(builder)
        .orderBy(orderSpecifier) // 동적 정렬
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

  private OrderSpecifier<?> getOrderSpecifier(QPost post, String type) {
    // type에 따라 정렬 기준 결정
    switch (type) {
      case "likes_count":
        return post.likes.count().desc(); // 찜 개수 기준 내림차순 정렬
      case "auction_ended_at":
        return post.auctionEndedAt.asc(); // 마감 날짜 기준 오름차순 정렬
      default:
        return post.createdAt.desc(); // 기본: 최신순 정렬
    }
  }
}
