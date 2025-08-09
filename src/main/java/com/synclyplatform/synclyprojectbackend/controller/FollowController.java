package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follows")
public class FollowController {

    private final FollowService followService;

    @GetMapping("/tags/{userId}")
    public ResponseEntity<List<TagDTO>> getFollowedTags(@PathVariable Long userId) {
        return new ResponseEntity<>(followService.getFollowedTags(userId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<UserProfileDTO>> getFollowedUsers(@PathVariable Long userId) {
        return new ResponseEntity<>(followService.getFollowedUsers(userId), HttpStatus.OK);
    }

    @PostMapping("/follow/tag/{userId}/{tagId}")
    public ResponseEntity<HttpStatus> followTag(@PathVariable Long userId, @PathVariable Long tagId) {
        followService.followTag(userId, tagId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/follow/user/{userProfileId}")
    public ResponseEntity<HttpStatus> followUser(@AuthenticationPrincipal User user, @PathVariable Long userProfileId) {
        followService.followUser(user.getUserId(), userProfileId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/unfollow/tag/{userId}/{tagId}")
    public ResponseEntity<HttpStatus> unfollowTag(@PathVariable Long userId, @PathVariable Long tagId) {
        followService.unfollowTag(userId, tagId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/follow/user/{userProfileId}")
    public ResponseEntity<HttpStatus> unfollowUser(@AuthenticationPrincipal User user, @PathVariable Long userProfileId) {
        followService.unfollowUser(user.getUserId(), userProfileId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
