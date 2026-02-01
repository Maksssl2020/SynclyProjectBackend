package com.synclyplatform.synclyprojectbackend.mapper;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityDTO;
import com.synclyplatform.synclyprojectbackend.model.activity.Activity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {

    public ActivityDTO toDTO(Activity activity) {
        return ActivityDTO.builder()
                .id(activity.getId())
                .userId(activity.getUser().getUserId())
                .actionType(activity.getActionType())
                .target(activity.getTarget())
                .targetType(activity.getTargetType())
                .timestamp(activity.getTimestamp().toString())
                .userRole(activity.getUser().getRole())
                .userUsername(activity.getUser().getUsername())
                .build();
    }
}
