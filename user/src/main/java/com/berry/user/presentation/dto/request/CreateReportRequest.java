package com.berry.user.presentation.dto.request;

import com.berry.user.domain.model.ReportType;
import jakarta.validation.constraints.NotNull;

public record CreateReportRequest(
    @NotNull
    Long reporterId,
    @NotNull
    Long reportedId,
    @NotNull
    ReportType reportType,
    @NotNull
    String reportReason,
    Long reviewId,
    Long postId
) {
}
