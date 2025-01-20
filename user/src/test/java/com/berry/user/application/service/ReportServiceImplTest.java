package com.berry.user.application.service;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.role.Role;
import com.berry.user.domain.model.Report;
import com.berry.user.domain.model.ReportStatus;
import com.berry.user.domain.model.ReportType;
import com.berry.user.domain.model.User;
import com.berry.user.domain.repository.ReportJpaRepository;
import com.berry.user.domain.repository.UserJpaRepository;
import com.berry.user.presentation.dto.request.CreateReportRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private ReportJpaRepository reportJpaRepository;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("유효한 요청일 때 신고가 저장되어야 한다")
    public void createReport_ShouldSaveReport_WhenRequestIsValid() {
        // given
        User reporter = User.builder()
            .id(1L)
            .nickname("reporter")
            .email("reporter@example.com")
            .password("password")
            .role(Role.MEMBER)
            .build();

        User reportedUser = User.builder()
            .id(2L)
            .nickname("reportedUser")
            .email("reported@example.com")
            .password("password")
            .role(Role.MEMBER)
            .build();

        CreateReportRequest request = new CreateReportRequest(
            1L, 2L, ReportType.PROFILE, "부적절한 닉네임이에요.", null, null);
        given(userJpaRepository.findById(1L)).willReturn(Optional.of(reporter));

        // when
        reportService.createReport(request, 1L);

        // then
        ArgumentCaptor<Report> reportCaptor = ArgumentCaptor.forClass(Report.class);
        then(reportJpaRepository).should().save(reportCaptor.capture());

        Report savedReport = reportCaptor.getValue();
        assertEquals(reporter, savedReport.getReporter());
        assertEquals(2L, savedReport.getReportedId());
        assertEquals(ReportType.PROFILE, savedReport.getReportType());
        assertEquals("부적절한 닉네임이에요.", savedReport.getReportReason());
        assertEquals(ReportStatus.PENDING, savedReport.getReportStatus());
    }

    @Test
    @DisplayName("사용자 ID가 올바르지 않을 때 예외가 발생해야 한다")
    public void createReport_ShouldThrowException_WhenUserIdsDoNotMatch() {
        // given
        CreateReportRequest request = new CreateReportRequest(
            1L, 2L, ReportType.PROFILE, "부적절한 닉네임이에요.", null, null);

        // when & then
        assertThrows(CustomApiException.class, () -> reportService.createReport(request, 99L));
    }

}