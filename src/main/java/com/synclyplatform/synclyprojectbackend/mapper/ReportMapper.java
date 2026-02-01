package com.synclyplatform.synclyprojectbackend.mapper;

import com.synclyplatform.synclyprojectbackend.dto.report.CommentReportDTO;
import com.synclyplatform.synclyprojectbackend.dto.report.PostReportDTO;
import com.synclyplatform.synclyprojectbackend.dto.report.ReportDTO;
import com.synclyplatform.synclyprojectbackend.model.report.CommentReport;
import com.synclyplatform.synclyprojectbackend.model.report.PostReport;
import com.synclyplatform.synclyprojectbackend.model.report.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportMapper {

    private final PostMapper postMapper;
    private final PostCommentMapper postCommentMapper;
    private final UserMapper userMapper;

    public ReportDTO toReportDTO(Report report) {
        if (report instanceof CommentReport commentReport) {
            return toCommentReportDTO(commentReport);
        } else if (report instanceof PostReport postReport) {
            return toPostReportDTO(postReport);
        }

        return ReportDTO.builder().build();
    }
    public PostReportDTO toPostReportDTO(PostReport postReport) {
        return PostReportDTO.builder()
                .id(postReport.getId())
                .entityId(String.format("POST_%s", postReport.getPost().getId()))
                .reason(postReport.getReason())
                .title(postReport.getTitle())
                .reportedBy(userMapper.toDTO(postReport.getReportedBy()))
                .resolvedBy(postReport.getResolvedBy() != null ? userMapper.toDTO(postReport.getResolvedBy()) : null)
                .reportedAt(postReport.getReportedAt().toString())
                .resolvedAt(postReport.getResolvedAt() != null ? postReport.getReportedAt().toString() : null)
                .post(postMapper.toDto(postReport.getPost()))
                .reportStatus(postReport.getReportStatus().name())
                .reportReasonType(postReport.getReportReasonType().name())
                .build();
    }
    public CommentReportDTO toCommentReportDTO(CommentReport commentReport) {
        return CommentReportDTO.builder()
                .id(commentReport.getId())
                .entityId(String.format("COMMENT_%s", commentReport.getComment().getId()))
                .reason(commentReport.getReason())
                .title(commentReport.getTitle())
                .reportedBy(userMapper.toDTO(commentReport.getReportedBy()))
                .resolvedBy(commentReport.getResolvedBy() != null ? userMapper.toDTO(commentReport.getResolvedBy()) : null)
                .reportedAt(commentReport.getResolvedAt() != null ? commentReport.getReportedAt().toString() : null)
                .resolvedAt(commentReport.getResolvedAt().toString())
                .comment(postCommentMapper.toDTO(commentReport.getComment()))
                .reportStatus(commentReport.getReportStatus().name())
                .reportReasonType(commentReport.getReportReasonType().name())
                .build();
    }
}
