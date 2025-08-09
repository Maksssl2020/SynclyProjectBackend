package com.synclyplatform.synclyprojectbackend.service.user_post_like;

import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface LikeService {

    void likePost(User user, long userId);
    void likeUserProfile(long userId, long userProfileId);
    void likePostComment(User user, long postCommentId);
}
