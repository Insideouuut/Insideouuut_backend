package com.goorm.insideout.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goorm.insideout.report.domain.Report;

public interface ReportRepository extends JpaRepository<Report,Long> {
}
