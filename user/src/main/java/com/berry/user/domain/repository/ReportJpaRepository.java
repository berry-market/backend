package com.berry.user.domain.repository;

import com.berry.user.domain.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportJpaRepository extends JpaRepository<Report, Long> {
}
