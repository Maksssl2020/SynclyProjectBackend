package com.synclyplatform.synclyprojectbackend.dto.user_profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequestDTO {

    private Long userId;
    private String displayName;
}
