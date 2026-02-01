package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.report.PostReport;
import com.synclyplatform.synclyprojectbackend.model.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReportRepository extends JpaRepository<PostReport,Long> {

    boolean existsByReportedByUserIdAndPostId(Long userId, Long postId);
}
