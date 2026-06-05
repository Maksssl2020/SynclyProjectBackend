package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.user_profile.AndroidUserProfileDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileUpdateRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.image.Image;
import com.synclyplatform.synclyprojectbackend.service.user_profile.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users-profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> findUserProfileByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(userProfileService.findByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/android-app/{userId}")
    public ResponseEntity<AndroidUserProfileDTO> findUserProfileByUserIdForAndroidApp(@PathVariable Long userId) {
        return new ResponseEntity<>(userProfileService.findByUserIdAndroid(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/avatar")
    public ResponseEntity<Image> fetchUserAvatar(@PathVariable Long userId) {
        return new ResponseEntity<>(userProfileService.getUserProfileAvatar(userId), HttpStatus.OK);
    }

    @PostMapping("/upload/avatar/{userId}")
    public ResponseEntity<HttpStatus> uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable Long userId) throws Exception {
        userProfileService.uploadAvatar(file, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<HttpStatus> updateUserProfile(@PathVariable Long userId, @ModelAttribute UserProfileUpdateRequestDTO userProfileUpdateRequest) throws Exception {
        userProfileService.updateUserProfile(userId, userProfileUpdateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
