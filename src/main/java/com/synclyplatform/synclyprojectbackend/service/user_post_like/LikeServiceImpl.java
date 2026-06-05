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
import java.util.List;

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
    public List<Long> getUserLikedProfilesIds(Long userId) {
        return userUserProfileLikeRepository.findAllLikedProfilesIdsByUserId(userId);
    }

    @Override
    public void likePost(User user, long postId) {
        if (userPostLikeRepository.existsByPostIdAndUserUserId(postId, user.getUserId())) {
            return;
        }

        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        UserPostLike userPostLike = UserPostLike.builder()
                .post(foundPost)
                .user(user)
                .likedAt(LocalDateTime.now())
                .build();

        userPostLikeRepository.save(userPostLike);
    }

    @Override
    public void likePost(Long userId, long postId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        likePost(foundUser, postId);
    }

    @Override
    public void unlikePost(User user, long postId) {
        UserPostLike foundPostLike = userPostLikeRepository.findByUserAndPostId(user, postId)
                .orElseThrow(() -> new RuntimeException("Post like not found."));

        userPostLikeRepository.delete(foundPostLike);
    }

    @Override
    public void unlikePost(Long userId, long postId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        unlikePost(foundUser, postId);
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
    public void unlikeUserProfile(long userId, long userProfileId) {
        if (userUserProfileLikeRepository.existsByUserUserIdAndUserProfileUserProfileId(userId, userProfileId)) {
            userUserProfileLikeRepository.deleteByUserUserIdAndUserProfileUserProfileId(userId, userProfileId);
        }
    }

    @Override
    public void likePostComment(User user, long postCommentId) {
        if (userCommentLikeRepository.existsByPostCommentIdAndUserUserId(postCommentId, user.getUserId())) {
            return;
        }

        PostComment foundPostComment = postCommentRepository.findById(postCommentId)
                .orElseThrow(() -> new RuntimeException("Post comment not found"));

        UserCommentLike userCommentLike = UserCommentLike.builder()
                .user(user)
                .postComment(foundPostComment)
                .likedAt(LocalDateTime.now())
                .build();

        userCommentLikeRepository.save(userCommentLike);
    }

    @Override
    public void likePostComment(Long userId, long postCommentId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        likePostComment(foundUser, postCommentId);
    }

    @Override
    public void unlikePostComment(User user, long postCommentId) {
        UserCommentLike foundPostCommentLike = userCommentLikeRepository.findByUserAndPostCommentId(user, postCommentId)
                .orElseThrow(() -> new RuntimeException("Post comment like not found."));

        userCommentLikeRepository.delete(foundPostCommentLike);
    }

    @Override
    public void unlikePostComment(Long userId, long postCommentId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        unlikePostComment(foundUser, postCommentId);
    }
}
