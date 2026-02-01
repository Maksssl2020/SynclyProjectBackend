package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.shared_post.SharedPostDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.service.shared_post.SharedPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shared-posts")
@RequiredArgsConstructor
public class SharedPostController {

    private final SharedPostService sharedPostService;

    @GetMapping("/by-authenticated-user")
    public ResponseEntity<List<SharedPostDTO>> getSharedPostsByAuthenticatedUser(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(sharedPostService.findAllByUser(user));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<SharedPostDTO>> getSharedPostsByUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(sharedPostService.findAllByUserId(userId));
    }

    @PostMapping("/share/{postId}")
    public ResponseEntity<HttpStatus> sharePost(@AuthenticationPrincipal User user, @PathVariable("postId") Long postId) {
        sharedPostService.sharePost(user, postId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/android-app/share")
    public ResponseEntity<HttpStatus> sharePostAndroid(@RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
        sharedPostService.sharePost(userId, postId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/unshare/{postId}")
    public ResponseEntity<HttpStatus> unsharePost(@AuthenticationPrincipal User user, @PathVariable("postId") Long postId) {
        sharedPostService.unsharePost(user, postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/android-app/unshare")
    public ResponseEntity<HttpStatus> unsharePostAndroid(@RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
        sharedPostService.unsharePost(userId, postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
