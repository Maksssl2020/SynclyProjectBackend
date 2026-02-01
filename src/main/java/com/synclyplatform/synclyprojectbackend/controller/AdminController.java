package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityDTO;
import com.synclyplatform.synclyprojectbackend.dto.admin.AdminPanelStatisticsDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UpdateUserDataAsAdminRequestDTO;
import com.synclyplatform.synclyprojectbackend.service.activity.ActivityService;
import com.synclyplatform.synclyprojectbackend.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/activity")
    public ResponseEntity<Page<ActivityDTO>> getAllActivities(
            @RequestParam("page")  int page,
            @RequestParam("size") int size
    ) {
        return new ResponseEntity<>(activityService.findALl(page, size), HttpStatus.OK);
    }

    @PatchMapping("/user/update/{userId}")
    public ResponseEntity<HttpStatus> updateUserProfile(
            @PathVariable Long userId,
            @ModelAttribute UpdateUserDataAsAdminRequestDTO updateUserDataAsAdminRequestDTO
    ) {
        adminService.updateUserAsAdmin(userId, updateUserDataAsAdminRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
