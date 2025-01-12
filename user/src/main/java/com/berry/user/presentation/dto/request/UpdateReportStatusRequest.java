package com.berry.user.presentation.dto.request;

import com.berry.user.domain.model.ReportStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateReportStatusRequest(
    @NotNull(message = "신고 아이디는 필수 입력 항목입니다.")
    Long reportId,
    @NotNull(message = "처리 상태는 필수 입력 항목입니다.")
    ReportStatus reportStatus
) {
}
