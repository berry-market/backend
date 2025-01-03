package com.berry.report.presentation.dto.response;

import com.berry.report.domain.model.ReportStatus;
import com.berry.report.domain.model.ReportType;

import java.time.LocalDateTime;

public record ReportResponse(
    Long reportId,
    Long reporterId,
    Long reportedUserId,
    ReportType reportType,
    String reportReason,
    ReportStatus reportStatus,
    LocalDateTime resolvedAt,
    LocalDateTime createdAt
) {
}
