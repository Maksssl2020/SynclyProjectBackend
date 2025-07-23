package com.synclyplatform.synclyprojectbackend.service.user_settings;

import com.synclyplatform.synclyprojectbackend.dto.user_settings.UserSettingsDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user_settings.UserSettings;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserSettingsRepository;
import com.synclyplatform.synclyprojectbackend.utils.UserSettingsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class UserSettingsServiceImpl implements UserSettingsService {

    private final UserRepository userRepository;
    private final UserSettingsRepository userSettingsRepository;
    private final UserSettingsMapper userSettingsMapper;

    @Override
    public UserSettings createUserSettings(Long userId) throws Exception {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found."));

        UserSettings userSettings = new UserSettings();
        userSettings.setUser(foundUser);

        return userSettingsRepository.save(userSettings);
    }

    @Override
    public UserSettingsDTO getUserSettings(Long userId) throws Exception {
        UserSettings foundUserSettings = userSettingsRepository.findByUserUserId(userId)
                .orElseThrow(() -> new Exception("User settings not found."));

        return  userSettingsMapper.toDto(foundUserSettings);
    }
}
