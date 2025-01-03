package com.berry.report.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.common.role.RoleCheck;
import com.berry.report.domain.model.ReportStatus;
import com.berry.report.domain.service.ReportService;
import com.berry.report.presentation.dto.response.ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/reports")
public class AdminReportController {

    private final ReportService reportService;

    @GetMapping
    @RoleCheck("ADMIN")
    public ResponseEntity<ApiResponse<Page<ReportResponse>>> getReports(
        @RequestHeader("X-User-Role") String role,
        Pageable pageable,
        @RequestParam ReportStatus reportStatus
    ) {
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, reportService.getReports(pageable, reportStatus)));
    }

}
