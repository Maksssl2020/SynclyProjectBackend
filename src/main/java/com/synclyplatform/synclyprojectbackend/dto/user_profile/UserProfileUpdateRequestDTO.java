package com.synclyplatform.synclyprojectbackend.dto.user_profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateRequestDTO {

    private String username;
    private String email;
    private String displayName;
    private String bio;
    private String location;
    private String website;
}
