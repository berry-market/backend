package com.berry.report.domain.service;

import com.berry.report.presentation.dto.request.CreateReportRequest;

public interface ReportService {
    void createReport(CreateReportRequest request, Long userId);
}
