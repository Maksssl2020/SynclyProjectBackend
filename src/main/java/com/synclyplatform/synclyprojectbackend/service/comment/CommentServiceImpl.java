package com.synclyplatform.synclyprojectbackend.service.comment;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentReplyRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.UpdateCommentRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import com.synclyplatform.synclyprojectbackend.model.comment.PostComment;
import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.repository.PostCommentRepository;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.mapper.PostCommentMapper;
import com.synclyplatform.synclyprojectbackend.service.activity.ActivityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;
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
    @Transactional
    public void deleteComment(User user, Long commentId) {
        PostComment foundComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!Objects.equals(foundComment.getAuthor().getUserId(), user.getUserId()) && (user.getRole().equals(UserRole.ADMIN) || user.getRole().equals(UserRole.MODERATOR))) {
            activityService.createActivity(
                    ActivityRequestDTO.builder()
                            .target(foundComment.getContent())
                            .userId(user.getUserId())
                            .targetId(commentId)
                            .actionType(ActivityActionType.DELETED)
                            .targetType(ActivityTargetType.COMMENT)
                            .build()
            );
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
