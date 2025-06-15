package com.synclyplatform.synclyprojectbackend.service.image;

import com.synclyplatform.synclyprojectbackend.model.Image;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.repository.ImageRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public void saveUserAvatar(Long userId, MultipartFile image) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found."));

        try {
            byte[] imageBytes = image.getBytes();
            Image imageEntity = Image.builder()
                    .imageData(imageBytes)
                    .build();

            imageRepository.save(imageEntity);
            userProfile.setProfileImage(imageEntity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
