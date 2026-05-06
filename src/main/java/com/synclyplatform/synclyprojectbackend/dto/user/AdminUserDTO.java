package com.synclyplatform.synclyprojectbackend.dto.user;

import com.synclyplatform.synclyprojectbackend.model.image.Image;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDTO {
    private Long userId;
    private String username;
    private String email;
    private UserRole role;
    private UserStatus status;
    private String createdAt;
    private String lastActive;
    private long postCount;
    private long followersCount;
    private Image avatar;
}
