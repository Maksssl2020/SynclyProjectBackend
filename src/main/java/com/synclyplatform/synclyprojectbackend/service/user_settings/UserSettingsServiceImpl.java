package com.synclyplatform.synclyprojectbackend.service.user_settings;

import com.synclyplatform.synclyprojectbackend.dto.user_settings.UserSettingUpdateRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_settings.UserSettingsDTO;
import com.synclyplatform.synclyprojectbackend.mapper.UserSettingsMapper;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user_settings.UserSettings;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserSettingsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSettingsServiceImpl implements UserSettingsService {

    private final UserRepository userRepository;
    private final UserSettingsRepository userSettingsRepository;
    private final UserSettingsMapper userSettingsMapper;

    @Override
    public UserSettings createUserSettings(Long userId)  {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        UserSettings userSettings = new UserSettings();
        userSettings.setUser(foundUser);

        return userSettingsRepository.save(userSettings);
    }

    @Override
    public UserSettingsDTO getUserSettings(Long userId) {
        UserSettings foundUserSettings = userSettingsRepository.findByUserUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User settings not found."));

        return  userSettingsMapper.toDto(foundUserSettings);
    }

    @Override
    public void updateUserSettings(UserSettingUpdateRequestDTO userSettingUpdateRequestDTO) {
        UserSettings foundUserSettings = userSettingsRepository.findById(userSettingUpdateRequestDTO.getUserSettingsId())
                .orElseThrow(() -> new EntityNotFoundException("User settings not found."));

        foundUserSettings.setPublicProfile(userSettingUpdateRequestDTO.isPublicProfile());
        foundUserSettings.setShowEmail(userSettingUpdateRequestDTO.isShowEmail());
        foundUserSettings.setShowLocation(userSettingUpdateRequestDTO.isShowLocation());
        foundUserSettings.setShowOnlineStatus(userSettingUpdateRequestDTO.isShowOnlineStatus());
        foundUserSettings.setTwoFactorAuthentication(userSettingUpdateRequestDTO.isTwoFactorAuthentication());

        userSettingsRepository.save(foundUserSettings);
    }
}
