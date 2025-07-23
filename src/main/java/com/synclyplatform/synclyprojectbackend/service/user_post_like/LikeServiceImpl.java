package com.synclyplatform.synclyprojectbackend.service.user_post_like;

import com.synclyplatform.synclyprojectbackend.model.comment.PostComment;
import com.synclyplatform.synclyprojectbackend.model.like.UserCommentLike;
import com.synclyplatform.synclyprojectbackend.model.like.UserPostLike;
import com.synclyplatform.synclyprojectbackend.model.like.UserUserProfileLike;
import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final UserPostLikeRepository userPostLikeRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserUserProfileLikeRepository userUserProfileLikeRepository;
    private final UserCommentLikeRepository userCommentLikeRepository;

    @Override
    public void likePost(long postId, long userId) {
        if (userPostLikeRepository.existsByPostIdAndUserUserId(postId, userId)) {
            return;
        }

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        UserPostLike userPostLike = UserPostLike.builder()
                .post(foundPost)
                .user(foundUser)
                .likedAt(LocalDateTime.now())
                .build();

        userPostLikeRepository.save(userPostLike);
    }

    @Override
    public void likeUserProfile(long userId, long userProfileId) {
        if (userUserProfileLikeRepository.existsByUserUserIdAndUserProfileUserProfileId(userId, userProfileId)) {
            return;
        }

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile foundUserProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        UserUserProfileLike userUserProfileLike = UserUserProfileLike.builder()
                .user(foundUser)
                .userProfile(foundUserProfile)
                .likedAt(LocalDateTime.now())
                .build();

        userUserProfileLikeRepository.save(userUserProfileLike);
    }

    @Override
    public void likePostComment(long userId, long postCommentId) {
        if (userCommentLikeRepository.existsByPostCommentIdAndUserUserId(postCommentId, userId)) {
            return;
        }

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PostComment foundPostComment = postCommentRepository.findById(postCommentId)
                .orElseThrow(() -> new RuntimeException("Post comment not found"));

        UserCommentLike userCommentLike = UserCommentLike.builder()
                .user(foundUser)
                .postComment(foundPostComment)
                .likedAt(LocalDateTime.now())
                .build();

        userCommentLikeRepository.save(userCommentLike);
    }
}
