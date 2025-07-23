package com.synclyplatform.synclyprojectbackend.service.user_settings;

import com.synclyplatform.synclyprojectbackend.dto.user_settings.UserSettingsDTO;
import com.synclyplatform.synclyprojectbackend.model.user_settings.UserSettings;
import org.springframework.stereotype.Service;

@Service
public interface UserSettingsService {

    UserSettings createUserSettings(Long userId) throws Exception;
    UserSettingsDTO getUserSettings(Long userId) throws Exception;
}
