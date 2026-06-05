package com.synclyplatform.synclyprojectbackend.service.user_profile;

import com.synclyplatform.synclyprojectbackend.dto.user_profile.AndroidUserProfileDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileUpdateRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.image.Image;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserProfileService {

    UserProfileDTO findByUserId(long userId);
    AndroidUserProfileDTO findByUserIdAndroid(long userId);
    Image getUserProfileAvatar(long userId);
    UserProfile createUserProfile(UserProfileRequestDTO userProfileRequest) throws Exception;
    Image uploadAvatar(MultipartFile avatarFile, Long userId) throws Exception;
    UserProfileDTO updateUserProfile(Long userId, UserProfileUpdateRequestDTO userProfileUpdateRequest) throws Exception;
    AndroidUserProfileDTO updateUserProfileAndroidApp(Long userId, UserProfileUpdateRequestDTO userProfileUpdateRequest) throws Exception;
}
