package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentReplyRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.UpdateCommentRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<PostCommentDTO>> getAllCommentsForPost(@PathVariable Long postId) {
        return new ResponseEntity<>(commentService.getCommentsForPost(postId), HttpStatus.OK);
    }

    @PostMapping("/add-comment-to-post")
    public ResponseEntity<PostCommentDTO> addCommentToPost(@RequestBody @Valid PostCommentRequestDTO postCommentRequest) {
        PostCommentDTO addedComment = commentService.addComment(postCommentRequest);
        return ResponseEntity.ok(addedComment);
    }

    @PostMapping("/add-comment-reply")
    public ResponseEntity<PostCommentDTO> addCommentReply(@RequestBody @Valid PostCommentReplyRequestDTO postCommentReplyRequest) {
        PostCommentDTO postCommentReplyDTO = commentService.addReply(postCommentReplyRequest);
        return ResponseEntity.ok(postCommentReplyDTO);
    }

    @PatchMapping("/update")
    public ResponseEntity<PostCommentDTO> updateComment(@RequestBody UpdateCommentRequestDTO updateCommentRequestDTO) {
        commentService.updateComment(updateCommentRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete-comment")
    public ResponseEntity<HttpStatus> deleteComment(@AuthenticationPrincipal User user, @RequestParam("commentId") Long commentId) {
        commentService.deleteComment(user, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
