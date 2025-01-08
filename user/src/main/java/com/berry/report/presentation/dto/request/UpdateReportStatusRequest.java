package com.berry.report.presentation.dto.request;

import com.berry.report.domain.model.ReportStatus;
import jakarta.validation.constraints.NotBlank;

public record UpdateReportStatusRequest(
    @NotBlank(message = "신고 아이디는 필수 입력 항목입니다.")
    Long reportId,
    @NotBlank(message = "처리 상태는 필수 입력 항목입니다.")
    ReportStatus reportStatus
) {
}
