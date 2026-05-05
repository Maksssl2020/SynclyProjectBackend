package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityDTO;
import com.synclyplatform.synclyprojectbackend.dto.admin.AdminPanelStatisticsDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UpdateUserDataAsAdminRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.utils.TimestampSortOption;
import com.synclyplatform.synclyprojectbackend.service.activity.ActivityService;
import com.synclyplatform.synclyprojectbackend.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final ActivityService activityService;

    @GetMapping("/panel/statistics")
    public ResponseEntity<AdminPanelStatisticsDTO> getAdminPanelStatistics() {
        return new ResponseEntity<>(adminService.getAdminStatistics(), HttpStatus.OK);
    }

    @GetMapping("/activity/most-recent")
    public ResponseEntity<List<ActivityDTO>> getMostRecentActivities() {
        return new ResponseEntity<>(activityService.findMostRecentActivities(), HttpStatus.OK);
    }

    @GetMapping("/activity/all")
    public ResponseEntity<Page<ActivityDTO>> getAllActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ActivityActionType actionType,
            @RequestParam(required = false) ActivityTargetType targetType,
            @RequestParam(required = false, defaultValue = "RECENT") TimestampSortOption sortOption,
            @RequestParam(required = false) String searchQuery
    ) {
        return new ResponseEntity<>(activityService.findAll(page, size, actionType, targetType, sortOption, searchQuery), HttpStatus.OK);
    }

    @PatchMapping("/user/update/{userId}")
    public ResponseEntity<HttpStatus> updateUserProfile(
            @AuthenticationPrincipal User adminUser,
            @PathVariable Long userId,
            @ModelAttribute UpdateUserDataAsAdminRequestDTO updateUserDataAsAdminRequestDTO
    ) {
        adminService.updateUserAsAdmin(userId, adminUser, updateUserDataAsAdminRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
