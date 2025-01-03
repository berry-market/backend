package com.berry.report.presentation.dto.request;

import com.berry.report.domain.model.ReportType;

public record CreateReportRequest(
    Long reporterId,
    Long reportedUserId,
    ReportType reportType,
    String reportReason
) {
}
