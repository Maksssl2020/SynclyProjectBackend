package com.synclyplatform.synclyprojectbackend.service.admin;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.admin.AdminPanelStatisticsDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UpdateUserDataAsAdminRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import com.synclyplatform.synclyprojectbackend.model.report.ReportStatus;
import com.synclyplatform.synclyprojectbackend.model.tag.TagType;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.ReportRepository;
import com.synclyplatform.synclyprojectbackend.repository.TagRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.activity.ActivityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final ActivityService activityService;

    @Override
    public AdminPanelStatisticsDTO getAdminStatistics() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        Long totalUsers = userRepository.countAllByRole(UserRole.REGISTERED);
        Long usersToday = userRepository.countAllByRoleAndCreatedAtBetween(
                UserRole.REGISTERED, today.atStartOfDay(), today.atTime(LocalTime.MAX)
        );
        Long usersYesterday = userRepository.countAllByRoleAndCreatedAtBetween(
                UserRole.REGISTERED, yesterday.atStartOfDay(), yesterday.atTime(LocalTime.MAX)
        );
        Long usersDiff = usersToday - usersYesterday;

        Long totalTags = tagRepository.countAllByType(TagType.MAIN);
        Long tagsToday = tagRepository.countAllByTypeAndCreatedAtBetween(
                TagType.MAIN, today.atStartOfDay(), today.atTime(LocalTime.MAX)
        );
        Long tagsYesterday = tagRepository.countAllByTypeAndCreatedAtBetween(
                TagType.MAIN, yesterday.atStartOfDay(), yesterday.atTime(LocalTime.MAX)
        );
        Long tagsDiff = tagsToday - tagsYesterday;

        Long totalReports = reportRepository.countAllByReportStatus(ReportStatus.PENDING);
        Long reportsToday = reportRepository.countAllByReportStatusAndReportedAtBetween(
                ReportStatus.PENDING, today.atStartOfDay(), today.atTime(LocalTime.MAX)
        );
        Long reportsYesterday = reportRepository.countAllByReportStatusAndReportedAtBetween(
                ReportStatus.PENDING, yesterday.atStartOfDay(), yesterday.atTime(LocalTime.MAX)
        );
        Long reportsDiff = reportsToday - reportsYesterday;

        Long postsToday = postRepository.countAllByCreatedAtBetween(
                today.atStartOfDay(), today.atTime(LocalTime.MAX)
        );
        Long postsYesterday = postRepository.countAllByCreatedAtBetween(
                yesterday.atStartOfDay(), yesterday.atTime(LocalTime.MAX)
        );
        Long postsDiff = postsToday - postsYesterday;

        return AdminPanelStatisticsDTO.builder()
                .totalUsersCount(totalUsers)
                .usersChangeFromYesterday(usersDiff)
                .totalMainTagsCount(totalTags)
                .tagsChangeFromYesterday(tagsDiff)
                .totalActiveReportsCount(totalReports)
                .reportsChangeFromYesterday(reportsDiff)
                .postsToday(postsToday)
                .postsChangeFromYesterday(postsDiff)
                .build();
    }

    @Override
    @Transactional
    public void updateUserAsAdmin(Long userId, User adminUser, UpdateUserDataAsAdminRequestDTO updateUserDataAsAdminRequestDTO) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        if (updateUserDataAsAdminRequestDTO.getBio() != null) {
            foundUser.getUserProfile().setBio(updateUserDataAsAdminRequestDTO.getBio());
            createUserActivity(adminUser, foundUser, ActivityActionType.UPDATED);
        }

        if (updateUserDataAsAdminRequestDTO.getUserStatus() != null && updateUserDataAsAdminRequestDTO.getUserStatus().equals(UserStatus.BLOCKED)) {
            foundUser.setAccountNonLocked(false);
            createUserActivity(adminUser, foundUser, ActivityActionType.BLOCKED);
        }

        if (updateUserDataAsAdminRequestDTO.getUserRole() != null) {
            foundUser.setRole(updateUserDataAsAdminRequestDTO.getUserRole());
            createUserActivity(adminUser, foundUser, ActivityActionType.PROMOTED);
        }

        userRepository.save(foundUser);
    }

    @Override
    @Transactional
    public void blockUserById(User adminUser, Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
        foundUser.setAccountNonLocked(false);

        createUserActivity(adminUser, foundUser, ActivityActionType.BLOCKED);
        userRepository.save(foundUser);
    }

    @Override
    @Transactional
    public void unblockUserById(User adminUser, Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
        foundUser.setAccountNonLocked(true);

        createUserActivity(adminUser, foundUser, ActivityActionType.UNBLOCKED);
        userRepository.save(foundUser);
    }

    private void createUserActivity(
            User adminUser,
            User targetUser,
            ActivityActionType actionType
    ) {
        activityService.createActivity(ActivityRequestDTO.builder()
                .userId(adminUser.getUserId())
                .targetId(targetUser.getUserId())
                .target(targetUser.getUsername())
                .actionType(actionType)
                .targetType(ActivityTargetType.USER)
                .build());
    }
}
