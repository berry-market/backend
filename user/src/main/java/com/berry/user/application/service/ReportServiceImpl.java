package com.berry.user.application.service;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.user.domain.model.Report;
import com.berry.user.domain.model.ReportStatus;
import com.berry.user.domain.repository.ReportJpaRepository;
import com.berry.user.domain.service.ReportService;
import com.berry.user.infrastructure.repository.ReportQueryRepository;
import com.berry.user.presentation.dto.request.CreateReportRequest;
import com.berry.user.presentation.dto.request.UpdateReportStatusRequest;
import com.berry.user.presentation.dto.response.ReportResponse;
import com.berry.user.domain.model.User;
import com.berry.user.domain.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportJpaRepository reportJpaRepository;
    private final ReportQueryRepository reportQueryRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public void createReport(CreateReportRequest request, Long userId) {
        if (!Objects.equals(userId, request.reporterId())) {
            throw new CustomApiException(ResErrorCode.FORBIDDEN);
        }
        User reporter = getUser(request.reporterId());
        User reportedUser = getUser(request.reportedUserId());

        Report report = Report.builder()
            .reporter(reporter)
            .reportedUser(reportedUser)
            .reportType(request.reportType())
            .reportReason(request.reportReason())
            .createdAt(LocalDateTime.now())
            .reportStatus(ReportStatus.PENDING)
            .build();
        reportJpaRepository.save(report);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportResponse> getReports(Pageable pageable, ReportStatus reportStatus) {
        return reportQueryRepository.getReports(pageable, reportStatus);
    }

    @Override
    @Transactional
    public void updateReportStatus(UpdateReportStatusRequest request) {
        Report report = reportJpaRepository.findById(request.reportId())
            .orElseThrow(() -> new CustomApiException(ResErrorCode.NOT_FOUND, "해당 신고를 찾을 수 없습니다."));
        report.updateReportStatus(request);
    }

    private User getUser(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(ResErrorCode.NOT_FOUND.getMessage()));
    }
}
