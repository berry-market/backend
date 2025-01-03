package com.berry.report.domain.service;

import com.berry.report.domain.model.ReportStatus;
import com.berry.report.presentation.dto.request.CreateReportRequest;
import com.berry.report.presentation.dto.response.ReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    void createReport(CreateReportRequest request, Long userId);

    Page<ReportResponse> getReports(Pageable pageable, ReportStatus reportStatus);
}
