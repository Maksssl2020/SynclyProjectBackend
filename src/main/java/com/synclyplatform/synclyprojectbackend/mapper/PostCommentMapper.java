package com.synclyplatform.synclyprojectbackend.mapper;

import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentDTO;
import com.synclyplatform.synclyprojectbackend.model.comment.PostComment;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostCommentMapper {

    private final UserProfileRepository userProfileRepository;

    public PostCommentDTO toDTO(PostComment postComment) {
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
                        .map(this::toDTO)
                        .toList() : List.of())
                .build();
    }
}
