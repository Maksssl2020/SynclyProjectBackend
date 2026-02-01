package com.synclyplatform.synclyprojectbackend.service.activity;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityDTO;
import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityRequestDTO;
import com.synclyplatform.synclyprojectbackend.mapper.ActivityMapper;
import com.synclyplatform.synclyprojectbackend.model.activity.Activity;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.ActivityRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ActivityMapper activityMapper;

    @Override
    public Page<ActivityDTO> findALl(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<Activity> activities = activityRepository.findAll(pageable);

        return activities.map(activityMapper::toDTO);
    }

    @Override
    public void createActivity(ActivityRequestDTO activityRequestDTO) {
        User foundUser = userRepository.findById(activityRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Activity activity = Activity.builder()
                .user(foundUser)
                .target(activityRequestDTO.getTarget())
                .actionType(activityRequestDTO.getActionType())
                .targetType(activityRequestDTO.getTargetType())
                .timestamp(LocalDateTime.now())
                .build();

        activityRepository.save(activity);
    }
}
