package com.berry.user.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.user.application.service.ReportServiceImpl;
import com.berry.user.domain.model.ReportType;
import com.berry.user.presentation.dto.request.CreateReportRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @InjectMocks
    private ReportController reportController;

    @Mock
    private ReportServiceImpl reportServiceImpl;

    @Captor
    private ArgumentCaptor<Long> userIdCaptor;

    @Captor
    private ArgumentCaptor<CreateReportRequest> requestCaptor;

    @Test
    @DisplayName("신고 생성 성공 테스트")
    void createReport() {
        // given
        CreateReportRequest request = new CreateReportRequest(
            1L, 2L, ReportType.PROFILE, "부적절한 닉네임이에요.", null, null);
        Long userId = 1L;

        // when
        ApiResponse<Void> response = reportController.createReport(request, userId);

        // then
        then(reportServiceImpl).should().createReport(requestCaptor.capture(), userIdCaptor.capture());

        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(request, requestCaptor.getValue());
    }
}