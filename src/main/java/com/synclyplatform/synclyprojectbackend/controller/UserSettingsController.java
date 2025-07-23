package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.user_settings.UserSettingsDTO;
import com.synclyplatform.synclyprojectbackend.service.user_settings.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users-settings")
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserSettingsDTO> getUserSettings(@PathVariable Long userId) throws Exception {
        return ResponseEntity.ok(userSettingsService.getUserSettings(userId));
    }
}
