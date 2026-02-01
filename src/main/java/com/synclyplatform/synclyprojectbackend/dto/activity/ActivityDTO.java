package com.synclyplatform.synclyprojectbackend.dto.activity;

import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {

    private Long id;
    private Long userId;
    private ActivityActionType actionType;
    private String userUsername;
    private UserRole userRole;
    private String target;
    private String timestamp;
    private ActivityTargetType targetType;
}
