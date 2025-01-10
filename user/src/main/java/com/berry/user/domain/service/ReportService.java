package com.berry.user.domain.service;

import com.berry.user.domain.model.ReportStatus;
import com.berry.user.presentation.dto.request.CreateReportRequest;
import com.berry.user.presentation.dto.request.UpdateReportStatusRequest;
import com.berry.user.presentation.dto.response.ReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    void createReport(CreateReportRequest request, Long userId);

    Page<ReportResponse> getReports(Pageable pageable, ReportStatus reportStatus);

    void updateReportStatus(UpdateReportStatusRequest request);
}
