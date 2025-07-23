package com.synclyplatform.synclyprojectbackend.service.user_post_like;

import org.springframework.stereotype.Service;

@Service
public interface LikeService {

    void likePost(long postId, long userId);
    void likeUserProfile(long userId, long userProfileId);
    void likePostComment(long userId, long postCommentId);
}
