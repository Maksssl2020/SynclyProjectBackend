package com.synclyplatform.synclyprojectbackend.service.activity;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityDTO;
import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.activity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityService {

    Page<ActivityDTO> findALl(int page, int size);
    void createActivity(ActivityRequestDTO activityRequestDTO);
}
