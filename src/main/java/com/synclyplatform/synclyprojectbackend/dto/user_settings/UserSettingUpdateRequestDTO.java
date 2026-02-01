package com.synclyplatform.synclyprojectbackend.dto.user_settings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingUpdateRequestDTO {

    private Long userSettingsId;
    private boolean publicProfile;
    private boolean showEmail;
    private boolean showLocation;
    private boolean showOnlineStatus;
    private boolean twoFactorAuthentication;
}
