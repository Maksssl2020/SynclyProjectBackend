package com.synclyplatform.synclyprojectbackend.service.user_profile;

import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import org.springframework.stereotype.Service;

@Service
public interface UserProfileService {

    UserProfile createUserProfile(UserProfileRequestDTO userProfileRequest) throws Exception;
}
