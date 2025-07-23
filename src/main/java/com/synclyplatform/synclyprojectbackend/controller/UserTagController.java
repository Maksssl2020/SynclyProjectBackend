package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import com.synclyplatform.synclyprojectbackend.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users-tags")
public class UserTagController {

    private final FollowService followService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<TagDTO>> getFollowedTags(@PathVariable Long userId) {
        return new ResponseEntity<>(followService.getFollowedTags(userId), HttpStatus.OK);
    }

    @PostMapping("/follow/{userId}/{tagId}")
    public ResponseEntity<HttpStatus> follow(@PathVariable Long userId, @PathVariable Long tagId) {
        followService.followTag(userId, tagId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/unfollow/{userId}/{tagId}")
    public ResponseEntity<HttpStatus> unfollow(@PathVariable Long userId, @PathVariable Long tagId) {
        followService.unfollowTag(userId, tagId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
