package com.berry.report.presentation.dto.request;

import com.berry.report.domain.model.ReportType;
import jakarta.validation.constraints.NotNull;

public record CreateReportRequest(
    @NotNull
    Long reporterId,
    @NotNull
    Long reportedUserId,
    @NotNull
    ReportType reportType,
    @NotNull
    String reportReason,
    Long reviewId,
    Long postId
) {
}
