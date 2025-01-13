package com.berry.user.infrastructure.repository;


import com.berry.user.domain.model.QReport;
import com.berry.user.domain.model.ReportStatus;
import com.berry.user.presentation.dto.response.ReportResponse;
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
public class ReportQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QReport report = QReport.report;

    public Page<ReportResponse> getReports(Pageable pageable, ReportStatus reportStatus) {
        List<ReportResponse> reportList = jpaQueryFactory
            .select(Projections.constructor(
                ReportResponse.class,
                report.id,
                report.reporter.id,
                report.reporter.nickname,
                report.reportedId,
                report.reportType,
                report.reportReason,
                report.reportStatus,
                report.resolvedAt,
                report.createdAt
            ))
            .from(report)
            .where(
                report.reportStatus.eq(reportStatus)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = jpaQueryFactory
            .select(report.count())
            .from(report)
            .where(
                report.reportStatus.eq(reportStatus)
            )
            .fetchOne();

        return new PageImpl<>(reportList, pageable, total != null ? total : 0L);
    }
}
