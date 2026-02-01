package com.synclyplatform.synclyprojectbackend.service.report;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.report.ReportDTO;
import com.synclyplatform.synclyprojectbackend.dto.report.ReportRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.report.ResolveReportRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import com.synclyplatform.synclyprojectbackend.model.comment.PostComment;
import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.report.*;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.*;
import com.synclyplatform.synclyprojectbackend.mapper.ReportMapper;
import com.synclyplatform.synclyprojectbackend.service.activity.ActivityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PostReportRepository postReportRepository;
    private final PostCommentReportRepository postCommentReportRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final ReportMapper reportMapper;
    private final ActivityService activityService;

    @Override
    public List<ReportDTO> findAll() {
        return reportRepository.findAll().stream()
                .map(reportMapper::toReportDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void report(User user, ReportRequestDTO reportRequestDTO) {
        if (reportRequestDTO.getReportType().equals(ReportType.POST)) {
            if (postReportRepository.existsByReportedByUserIdAndPostId(user.getUserId(), reportRequestDTO.getEntityId())) {
                throw new IllegalArgumentException("Post report already exists.");
            }

            PostReport postReport = reportPost(reportRequestDTO);
            postReport.setReportedBy(user);
            reportRepository.save(postReport);
        } else {
            if (postCommentReportRepository.existsByReportedByUserIdAndCommentId(user.getUserId(), reportRequestDTO.getEntityId())) {
                throw new IllegalArgumentException("Post comment report already exists.");
            }

            CommentReport commentReport = reportComment(reportRequestDTO);
            commentReport.setReportedBy(user);
            reportRepository.save(commentReport);
        }
    }

    @Override
    public void resolve(User user, ResolveReportRequestDTO resolveReportRequestDTO) {
        Report foundReport = reportRepository.findById(resolveReportRequestDTO.getReportId())
                .orElseThrow(() -> new EntityNotFoundException("Report not found."));

        foundReport.setResolvedBy(user);
        foundReport.setReportStatus(resolveReportRequestDTO.getReportStatus());
        foundReport.setResolvedAt(LocalDateTime.now());

        reportRepository.save(foundReport);

        activityService.createActivity(
                ActivityRequestDTO.builder()
                        .actionType(getActivityActionType(resolveReportRequestDTO.getReportStatus()))
                        .target(foundReport.getTitle())
                        .userId(user.getUserId())
                        .targetType(ActivityTargetType.REPORT)
                        .build()
        );
    }

    private ActivityActionType getActivityActionType(ReportStatus reportStatus) {
        if (reportStatus == ReportStatus.RESOLVED) {
            return ActivityActionType.RESOLVED;
        } else if (reportStatus == ReportStatus.REJECTED) {
            return ActivityActionType.REJECTED;
        }

        return ActivityActionType.CREATED;
    }

    private PostReport reportPost(ReportRequestDTO reportRequestDTO) {
        Post foundPost = postRepository.findById(reportRequestDTO.getEntityId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found."));

        return PostReport.builder()
                .post(foundPost)
                .title(reportRequestDTO.getTitle())
                .reason(reportRequestDTO.getReason())
                .reportReasonType(reportRequestDTO.getReportReasonType())
                .reportStatus(ReportStatus.PENDING)
                .reportedAt(LocalDateTime.now())
                .build();
    }

    private CommentReport reportComment(ReportRequestDTO reportRequestDTO) {
        PostComment foundPostComment = postCommentRepository.findById(reportRequestDTO.getEntityId())
                .orElseThrow(() -> new EntityNotFoundException("Comment not found."));

        return CommentReport.builder()
                .comment(foundPostComment)
                .title(reportRequestDTO.getTitle())
                .reason(reportRequestDTO.getReason())
                .reportReasonType(reportRequestDTO.getReportReasonType())
                .reportStatus(ReportStatus.PENDING)
                .reportedAt(LocalDateTime.now())
                .build();
    }
}
