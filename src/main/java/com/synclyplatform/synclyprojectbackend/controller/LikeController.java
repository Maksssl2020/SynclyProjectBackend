package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.service.user_post_like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/user/liked-profiles")
    public ResponseEntity<List<Long>> getUserLikedProfilesIds(@RequestParam("userId") Long userId) {
        return new ResponseEntity<>(likeService.getUserLikedProfilesIds(userId), HttpStatus.OK);
    }

    @PostMapping("/like/post")
    public ResponseEntity<HttpStatus> likePost(@AuthenticationPrincipal User user, @RequestParam("postId") Long postId) {
        likeService.likePost(user, postId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/android-app/like/post")
    public ResponseEntity<HttpStatus> likePostAndroid(@RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
        likeService.likePost(userId, postId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/like/comment")
    public ResponseEntity<HttpStatus> likeComment(@AuthenticationPrincipal User user, @RequestParam("commentId") Long commentId) {
        likeService.likePostComment(user, commentId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/android-app/like/comment")
    public ResponseEntity<HttpStatus> likeCommentAndroid(@RequestParam("userId") Long userId, @RequestParam("commentId") Long commentId) {
        likeService.likePostComment(userId, commentId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/unlike/post")
    public ResponseEntity<HttpStatus> unlikePost(@AuthenticationPrincipal User user, @RequestParam("postId") Long postId) {
        likeService.unlikePost(user, postId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/android-app/unlike/post")
    public ResponseEntity<HttpStatus> unlikePostAndroid(@RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
        likeService.unlikePost(userId, postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/unlike/comment")
    public ResponseEntity<HttpStatus> unlikeComment(@AuthenticationPrincipal User user, @RequestParam("commentId") Long commentId) {
        likeService.unlikePostComment(user, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/android-app/unlike/comment")
    public ResponseEntity<HttpStatus> unlikeCommentAndroid(@RequestParam("userId") Long userId, @RequestParam("commentId") Long commentId) {
        likeService.unlikePostComment(userId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
