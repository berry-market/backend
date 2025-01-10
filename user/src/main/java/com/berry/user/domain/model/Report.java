package com.berry.user.domain.model;

import com.berry.user.presentation.dto.request.UpdateReportStatusRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id", nullable = false)
    private User reportedUser;

    @Column(name = "report_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(name = "report_reason", nullable = false)
    private String reportReason;

    @Column(name = "report_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus = ReportStatus.PENDING;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public void updateReportStatus(UpdateReportStatusRequest request) {
        this.reportStatus = request.reportStatus();
        this.resolvedAt = LocalDateTime.now();
    }
}
