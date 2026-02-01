package com.synclyplatform.synclyprojectbackend.mapper;

import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileDTO;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.repository.UserUserProfileLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileMapper {

    private final UserUserProfileLikeRepository userProfileLikeRepository;

    public UserProfileDTO toDTO(UserProfile userProfile) {
        Long likesCount = userProfileLikeRepository.countByUserProfileUserProfileId(userProfile.getUserProfileId());
        int followersCount = userProfile.getFollowers().size();
        int followedUsersCount = userProfile.getUser().getFollowedUsers().size();

        return UserProfileDTO.builder()
                .userProfileId(userProfile.getUserProfileId())
                .profileOwnerId(userProfile.getUser().getUserId())
                .username(userProfile.getUser().getUsername())
                .email(userProfile.getUser().getEmail())
                .bio(userProfile.getBio())
                .joinedAt(userProfile.getUser().getCreatedAt().toString())
                .birthday(userProfile.getBirthday() != null ? userProfile.getBirthday().toString() : "")
                .displayName(userProfile.getDisplayName())
                .location(userProfile.getLocation())
                .avatar(userProfile.getProfileImage())
                .website(userProfile.getWebsite())
                .profileLikes(likesCount)
                .followersCount(followersCount)
                .followingCount(followedUsersCount)
                .build();
    }
}
