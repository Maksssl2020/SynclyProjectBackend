package com.synclyplatform.synclyprojectbackend.mapper;

import com.synclyplatform.synclyprojectbackend.dto.report.*;
import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.model.report.CommentReport;
import com.synclyplatform.synclyprojectbackend.model.report.PostReport;
import com.synclyplatform.synclyprojectbackend.model.report.Report;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
                .resolvedBy(getResolvedByOrNull(postReport.getResolvedBy()))
                .reportedAt(getDateOrNull(postReport.getReportedAt()))
                .resolvedAt(getDateOrNull(postReport.getResolvedAt()))
                .post(postMapper.toDto(postReport.getPost()))
                .reportStatus(postReport.getReportStatus().name())
                .reportReasonType(postReport.getReportReasonType().name())
                .reportType(ReportType.POST)
                .build();
    }
    public CommentReportDTO toCommentReportDTO(CommentReport commentReport) {
        return CommentReportDTO.builder()
                .id(commentReport.getId())
                .entityId(String.format("COMMENT_%s", commentReport.getComment().getId()))
                .reason(commentReport.getReason())
                .title(commentReport.getTitle())
                .reportedBy(userMapper.toDTO(commentReport.getReportedBy()))
                .resolvedBy(getResolvedByOrNull(commentReport.getResolvedBy()))
                .reportedAt(getDateOrNull(commentReport.getReportedAt()))
                .resolvedAt(getDateOrNull(commentReport.getResolvedAt()))
                .comment(postCommentMapper.toDTO(commentReport.getComment()))
                .reportStatus(commentReport.getReportStatus().name())
                .reportReasonType(commentReport.getReportReasonType().name())
                .reportType(ReportType.COMMENT)
                .build();
    }

    private String getDateOrNull(LocalDateTime date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }

    private UserDTO getResolvedByOrNull(User user) {
        if (user == null) {
            return null;
        } else {
            return userMapper.toDTO(user);
        }
    }
}
