package com.synclyplatform.synclyprojectbackend.service.comment;

import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentReplyRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.UpdateCommentRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.comment.PostComment;
import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.PostCommentRepository;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.mapper.PostCommentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCommentMapper postCommentMapper;

    @Override
    public PostCommentDTO addComment(PostCommentRequestDTO postCommentRequest) {
        Post post = postRepository.findById(postCommentRequest.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        User author = userRepository.findById(postCommentRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        PostComment comment = PostComment.builder()
                .post(post)
                .author(author)
                .content(postCommentRequest.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return postCommentMapper.toDTO(postCommentRepository.save(comment));
    }

    @Override
    public PostCommentDTO addReply(PostCommentReplyRequestDTO postCommentReplyRequest) {
        PostComment parent = postCommentRepository.findById(postCommentReplyRequest.getParentCommentId())
                .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));

        User author = userRepository.findById(postCommentReplyRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        PostComment reply = PostComment.builder()
                .post(parent.getPost())
                .author(author)
                .parent(parent)
                .content(postCommentReplyRequest.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return postCommentMapper.toDTO(postCommentRepository.save(reply));
    }

    @Override
    public List<PostCommentDTO> getCommentsForPost(Long postId) {
        List<PostComment> topLevelComments = postCommentRepository
                .findByPostIdAndParentIsNullOrderByCreatedAtAsc(postId);

        return topLevelComments.stream()
                .map(postCommentMapper::toDTO)
                .toList();
    }

    @Override
    public void deleteComment(Long commentId) {
        if (!postCommentRepository.existsById(commentId)) {
            throw new EntityNotFoundException("Comment not found");
        }

        postCommentRepository.deleteById(commentId);
    }

    @Override
    public void updateComment(UpdateCommentRequestDTO updateCommentRequestDTO) {
        PostComment comment = postCommentRepository.findById(updateCommentRequestDTO.getCommentId())
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!comment.getContent().equals(updateCommentRequestDTO.getContent())) {
            comment.setContent(updateCommentRequestDTO.getContent());
            postCommentRepository.save(comment);
        }
    }
}
