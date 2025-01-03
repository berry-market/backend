package com.berry.report.domain.repository;

import com.berry.report.domain.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportJpaRepository extends JpaRepository<Report, Long> {
}
