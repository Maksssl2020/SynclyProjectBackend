package com.synclyplatform.synclyprojectbackend.service.comment;

import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentReplyRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.comment.PostComment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    PostCommentDTO addComment(PostCommentRequestDTO postCommentRequest);
    PostCommentDTO addReply(PostCommentReplyRequestDTO postCommentReplyRequest);
    List<PostCommentDTO> getCommentsForPost(Long postId);
    void deleteComment(Long commentId);
}
