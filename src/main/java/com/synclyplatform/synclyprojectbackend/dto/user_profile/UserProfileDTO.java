package com.synclyplatform.synclyprojectbackend.dto.user_profile;

import com.synclyplatform.synclyprojectbackend.model.image.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {

    private Long userProfileId;
    private Long profileOwnerId;
    private String username;
    private String email;
    private String displayName;
    private String bio;
    private String location;
    private String birthday;
    private String joinedAt;
    private long profileLikes;
    private int followersCount;
    private int followingCount;
    private String website;
    private Image avatar;
}
