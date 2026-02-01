package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.report.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentReportRepository extends JpaRepository<CommentReport, Long> {

    boolean existsByReportedByUserIdAndCommentId(Long userId, Long commentId);
}
