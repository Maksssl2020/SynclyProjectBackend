package com.synclyplatform.synclyprojectbackend.dto.activity;

import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequestDTO {

    private Long userId;
    private ActivityActionType actionType;
    private String target;
    private ActivityTargetType targetType;
}
