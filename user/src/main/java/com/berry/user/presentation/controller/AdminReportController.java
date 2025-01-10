package com.berry.user.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.common.role.RoleCheck;
import com.berry.user.domain.model.ReportStatus;
import com.berry.user.domain.service.ReportService;
import com.berry.user.presentation.dto.request.UpdateReportStatusRequest;
import com.berry.user.presentation.dto.response.ReportResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/reports")
public class AdminReportController {

    private final ReportService reportService;

    @GetMapping
    @RoleCheck("ADMIN")
    public ApiResponse<Page<ReportResponse>> getReports(
        @RequestHeader("X-Role") String role,
        Pageable pageable,
        @RequestParam ReportStatus reportStatus
    ) {
        return ApiResponse.OK(ResSuccessCode.READ, reportService.getReports(pageable, reportStatus));
    }

    @PatchMapping
    @RoleCheck("ADMIN")
    public ApiResponse<Void> updateReportStatus(
        @RequestHeader("X-Role") String role,
        @RequestBody @Valid UpdateReportStatusRequest request
    ) {
        reportService.updateReportStatus(request);
        return ApiResponse.OK(ResSuccessCode.UPDATED, "신고된 내용이 적절히 처리되었습니다.");
    }

}
