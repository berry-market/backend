package com.berry.user.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.user.domain.service.ReportService;
import com.berry.user.presentation.dto.request.CreateReportRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;


    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createReport(
        @Valid @RequestBody CreateReportRequest request,
        @RequestHeader("X-UserId") Long userId
    ) {
        reportService.createReport(request, userId);
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.CREATED, "신고가 접수되었습니다."));
    }

}