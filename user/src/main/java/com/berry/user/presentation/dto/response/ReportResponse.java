package com.berry.user.presentation.dto.response;

import com.berry.user.domain.model.ReportStatus;
import com.berry.user.domain.model.ReportType;

import java.time.LocalDateTime;

public record ReportResponse(
    Long reportId,
    Long reporterId,
    String reporterName,
    Long reportedId,
    ReportType reportType,
    String reportReason,
    ReportStatus reportStatus,
    LocalDateTime resolvedAt,
    LocalDateTime createdAt
) {
}
