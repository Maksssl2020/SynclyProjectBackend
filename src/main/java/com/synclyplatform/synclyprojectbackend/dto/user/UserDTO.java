package com.synclyplatform.synclyprojectbackend.dto.user;

import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileDTO;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private UserStatus status;
    private String createdAt;
    private String lastActive;
    private UserRole role;
    private UserProfileDTO userProfile;
}
