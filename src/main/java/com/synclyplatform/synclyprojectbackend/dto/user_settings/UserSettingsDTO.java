package com.synclyplatform.synclyprojectbackend.dto.user_settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingsDTO {

    private Long userSettingsId;
    private boolean publicProfile = true;
    private boolean showEmail = false;
    private boolean showLocation = true;
    private boolean showOnlineStatus = true;
    private boolean twoFactorAuthentication = false;
}
