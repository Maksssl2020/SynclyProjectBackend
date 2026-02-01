package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.user_settings.UserSettingUpdateRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_settings.UserSettingsDTO;
import com.synclyplatform.synclyprojectbackend.service.user_settings.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users-settings")
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserSettingsDTO> getUserSettings(@PathVariable Long userId) throws Exception {
        return ResponseEntity.ok(userSettingsService.getUserSettings(userId));
    }

    @PatchMapping("/update")
    public ResponseEntity<HttpStatus> updateUserSettings(@RequestBody UserSettingUpdateRequestDTO userSettingUpdateRequestDTO) throws Exception {
        userSettingsService.updateUserSettings(userSettingUpdateRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
