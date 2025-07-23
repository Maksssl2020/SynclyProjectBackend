package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.service.user_post_like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like/post")
    public ResponseEntity<HttpStatus> likePost(@RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
        likeService.likePost(userId, postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/like/comment")
    public ResponseEntity<HttpStatus> likeComment(@RequestParam("userId") Long userId, @RequestParam("commentId") Long commentId) {
        likeService.likePostComment(userId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
