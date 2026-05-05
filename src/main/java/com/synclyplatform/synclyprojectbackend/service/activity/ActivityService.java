package com.synclyplatform.synclyprojectbackend.service.activity;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityDTO;
import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import com.synclyplatform.synclyprojectbackend.model.utils.TimestampSortOption;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityService {

    List<ActivityDTO> findMostRecentActivities();
    Page<ActivityDTO> findAll(int page, int size, ActivityActionType actionType, ActivityTargetType targetType, TimestampSortOption sortOption, String searchQuery);
    void createActivity(ActivityRequestDTO activityRequestDTO);

}
