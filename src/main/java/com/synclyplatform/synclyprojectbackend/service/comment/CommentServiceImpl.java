package com.synclyplatform.synclyprojectbackend.service.comment;

import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentReplyRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.comment.PostComment;
import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.repository.PostCommentRepository;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserProfileRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

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

        return createPostCommentDTO(postCommentRepository.save(comment));
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

        return createPostCommentDTO(postCommentRepository.save(reply));
    }

    @Override
    public List<PostCommentDTO> getCommentsForPost(Long postId) {
        List<PostComment> topLevelComments = postCommentRepository
                .findByPostIdAndParentIsNullOrderByCreatedAtAsc(postId);

        return topLevelComments.stream()
                .map(this::createPostCommentDTO)
                .toList();
    }

    @Override
    public void deleteComment(Long commentId) {
        if (!postCommentRepository.existsById(commentId)) {
            throw new EntityNotFoundException("Comment not found");
        }
        postCommentRepository.deleteById(commentId);
    }

    public PostCommentDTO createPostCommentDTO(PostComment postComment) {
        Optional<UserProfile> userProfile = userProfileRepository.findByUser_UserId(postComment.getAuthor().getUserId());

        return PostCommentDTO.builder()
                .id(postComment.getId())
                .content(postComment.getContent())
                .createdAt(postComment.getCreatedAt().toString())
                .updatedAt(postComment.getUpdatedAt().toString())
                .authorId(postComment.getAuthor().getUserId())
                .authorUsername(postComment.getAuthor().getUsername())
                .authorName(userProfile.map(UserProfile::getDisplayName).orElse(postComment.getAuthor().getFirstName() + " "  + postComment.getAuthor().getLastName()))
                .authorImage(userProfile.map(UserProfile::getProfileImage).orElse(null))
                .likesBy(postComment.getLikes() != null ? postComment.getLikes().stream().map(userCommentLike ->  userCommentLike.getUser().getUserId()).toList() : List.of())
                .postId(postComment.getPost().getId())
                .parentId(postComment.getParent() != null ? postComment.getParent().getId() : null)
                .replies(postComment.getReplies() != null ? postComment.getReplies().stream()
                        .map(this::createPostCommentDTO)
                        .toList() : List.of())
                .build();
    }
}
