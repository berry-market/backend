package com.berry.user.infrastructure.repository;

import com.berry.user.domain.model.QUser;
import com.berry.user.presentation.dto.response.GetUserDetailResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QUser user = QUser.user;

    public Page<GetUserDetailResponse> getUsers(Pageable pageable) {
        List<GetUserDetailResponse> userList = jpaQueryFactory
            .select(Projections.constructor(
                GetUserDetailResponse.class,
                user.id,
                user.nickname,
                user.email,
                user.profileImage,
                user.point,
                user.role,
                user.deletedYN
            ))
            .from(user)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = jpaQueryFactory
            .select(user.count())
            .from(user)
            .fetchOne();

        return new PageImpl<>(userList, pageable, total != null ? total : 0L);
    }
}
