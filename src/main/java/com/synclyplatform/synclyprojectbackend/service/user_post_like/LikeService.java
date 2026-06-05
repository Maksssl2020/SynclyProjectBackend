package com.synclyplatform.synclyprojectbackend.service.user_post_like;

import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LikeService {

    List<Long> getUserLikedProfilesIds(Long userId);

    void likePost(User user, long postId);
    void likePost(Long userId, long postId);
    void unlikePost(User user, long postId);
    void unlikePost(Long userId, long postId);
    void likePostComment(User user, long postCommentId);
    void likePostComment(Long userId, long postCommentId);
    void unlikePostComment(User user, long postCommentId);
    void unlikePostComment(Long userId, long postCommentId);

    void likeUserProfile(long userId, long userProfileId);
    void unlikeUserProfile(long userId, long userProfileId);
}
