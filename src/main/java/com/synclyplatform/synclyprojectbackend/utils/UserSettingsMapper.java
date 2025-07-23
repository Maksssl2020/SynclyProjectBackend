package com.synclyplatform.synclyprojectbackend.utils;

import com.synclyplatform.synclyprojectbackend.dto.user_settings.UserSettingsDTO;
import com.synclyplatform.synclyprojectbackend.model.user_settings.UserSettings;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public abstract class UserSettingsMapper {

    @Mappings({
            @Mapping(source = "userSettingsId", target = "userSettingsId"),
            @Mapping(source = "showEmail", target = "showEmail"),
            @Mapping(source = "showLocation", target = "showLocation"),
            @Mapping(source = "showOnlineStatus", target = "showOnlineStatus"),
            @Mapping(source = "twoFactorAuthentication", target = "twoFactorAuthentication"),
            @Mapping(source = "publicProfile", target = "publicProfile"),
    })
    public abstract UserSettingsDTO toDto(UserSettings userSettings);
}
