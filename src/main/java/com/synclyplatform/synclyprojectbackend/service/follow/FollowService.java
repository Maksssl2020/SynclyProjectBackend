package com.synclyplatform.synclyprojectbackend.service.follow;

import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FollowService {

    List<TagDTO> getFollowedTags(Long userId);
    List<UserProfileDTO> getFollowedUsers(Long userId);
    List<Long> getFollowedUsersIds(Long userId);

    void followTag(Long userId, Long tagId);
    void unfollowTag(Long userId, Long tagId);
    void followUser(Long userId, Long followedUserId);
    void unfollowUser(Long userId, Long followedUserId);
    UserProfileDTO followUserAndroid(Long userId, Long followedUserId);
    UserProfileDTO unfollowUserAndroid(Long userId, Long followedUserId);
}
