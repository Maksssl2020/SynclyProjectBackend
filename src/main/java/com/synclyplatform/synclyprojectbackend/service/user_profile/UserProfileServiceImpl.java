package com.synclyplatform.synclyprojectbackend.service.user_profile;

import com.synclyplatform.synclyprojectbackend.dto.user_profile.AndroidUserProfileDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileUpdateRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.image.Image;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.repository.FriendRepository;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserProfileRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.media.MediaService;
import com.synclyplatform.synclyprojectbackend.mapper.UserProfileMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final MediaService mediaService;
    private final PostRepository postRepository;
    private final FriendRepository friendRepository;

    @Override
    public UserProfileDTO findByUserId(long userId) {
        UserProfile foundUserProfile = userProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("User Profile Not Found!"));

        return userProfileMapper.toDTO(foundUserProfile);
    }

    @Override
    public AndroidUserProfileDTO findByUserIdAndroid(long userId) {
        UserProfileDTO foundProfile = findByUserId(userId);
        Long postsCount = postRepository.countByAuthorUserId(userId);
        Long friendsCount = friendRepository.countFriendsByUserId(userId);

        return userProfileMapper.toAndroidUserProfileDTOFromUserProfileDTO(foundProfile,  postsCount, friendsCount);
    }

    @Override
    @Transactional
    public Image getUserProfileAvatar(long userId) {
        return findByUserId(userId).getAvatar();
    }

    @Override
    @Transactional
    public UserProfile createUserProfile(UserProfileRequestDTO userProfileRequest) throws Exception {
        User foundUser = userRepository.findById(userProfileRequest.getUserId())
                .orElseThrow(() -> new Exception("User not found."));

        UserProfile userProfile = UserProfile.builder()
                .displayName(userProfileRequest.getDisplayName())
                .user(foundUser)
                .build();

        return userProfileRepository.save(userProfile);
    }

    @Override
    public Image uploadAvatar(MultipartFile avatarFile, Long userId) {
        return mediaService.saveUserAvatar(userId, avatarFile);
    }

    @Override
    @Transactional
    public UserProfileDTO updateUserProfile(Long userId, UserProfileUpdateRequestDTO userProfileUpdateRequest) throws Exception {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User Not Found."));
        UserProfile foundUserProfile = userProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new Exception("User Profile Not Found."));

        if (userProfileUpdateRequest.getDisplayName() != null) {
            foundUserProfile.setDisplayName(userProfileUpdateRequest.getDisplayName());
        }
        if (userProfileUpdateRequest.getBio() != null) {
            foundUserProfile.setBio(userProfileUpdateRequest.getBio());
        }
        if (userProfileUpdateRequest.getWebsite() != null) {
            foundUserProfile.setWebsite(userProfileUpdateRequest.getWebsite());
        }
        if (userProfileUpdateRequest.getLocation() != null) {
            foundUserProfile.setLocation(userProfileUpdateRequest.getLocation());
        }
        if (userProfileUpdateRequest.getEmail() != null) {
            foundUser.setEmail(userProfileUpdateRequest.getEmail());
        }
        if (userProfileUpdateRequest.getUsername() != null) {
            foundUser.setUsername(userProfileUpdateRequest.getUsername());
        }

        UserProfile savedProfile = userProfileRepository.save(foundUserProfile);
        userRepository.save(foundUser);

        return userProfileMapper.toDTO(savedProfile);
    }

    @Override
    @Transactional
    public AndroidUserProfileDTO updateUserProfileAndroidApp(Long userId, UserProfileUpdateRequestDTO userProfileUpdateRequest) throws Exception {
        UserProfileDTO updatedUserProfile = updateUserProfile(userId, userProfileUpdateRequest);
        Long postsCount = postRepository.countByAuthorUserId(userId);
        Long friendsCount = friendRepository.countFriendsByUserId(userId);

        return userProfileMapper.toAndroidUserProfileDTOFromUserProfileDTO(updatedUserProfile, postsCount, friendsCount);
    }
}
