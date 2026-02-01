package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.report.Report;
import com.synclyplatform.synclyprojectbackend.model.report.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Long countAllByReportStatus(ReportStatus reportStatus);
    Long countAllByReportStatusAndReportedAtBetween(ReportStatus reportStatus, LocalDateTime reportedAt, LocalDateTime reportedAt2);
}
