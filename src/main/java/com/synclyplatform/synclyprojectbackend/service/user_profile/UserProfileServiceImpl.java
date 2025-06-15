package com.synclyplatform.synclyprojectbackend.service.user_profile;

import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.repository.UserProfileRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfile createUserProfile(UserProfileRequestDTO userProfileRequest) throws Exception {
        User foundUser = userRepository.findById(userProfileRequest.getUserId())
                .orElseThrow(() -> new Exception("User not found."));

        UserProfile userProfile = UserProfile.builder()
                .displayName(userProfileRequest.getDisplayName())
                .user(foundUser)
                .build();

        return userProfileRepository.save(userProfile);
    }
}
