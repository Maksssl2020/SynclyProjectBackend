package com.synclyplatform.synclyprojectbackend.dto.user;

import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDataAsAdminRequestDTO {
    private UserRole userRole;
    private UserStatus userStatus;
    private String bio;
}
