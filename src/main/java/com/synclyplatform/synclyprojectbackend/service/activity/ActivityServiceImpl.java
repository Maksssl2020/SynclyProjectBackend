package com.synclyplatform.synclyprojectbackend.service.activity;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityDTO;
import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityRequestDTO;
import com.synclyplatform.synclyprojectbackend.mapper.ActivityMapper;
import com.synclyplatform.synclyprojectbackend.model.activity.Activity;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.utils.TimestampSortOption;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ActivityMapper activityMapper;

    @Override
    public List<ActivityDTO> findMostRecentActivities() {
        Pageable pageable = PageRequest.of(0, 6, Sort.Direction.DESC, "timestamp");
        Page<Activity> activityPage = activityRepository.findAll(pageable);

        return activityPage.getContent().stream()
                .map(activityMapper::toDTO)
                .toList();
    }

    @Override
    public Page<ActivityDTO> findAll(int page, int size, ActivityActionType actionType, ActivityTargetType targetType, TimestampSortOption sortOption, String searchQuery) {
        Sort sort = switch (sortOption) {
            case RECENT -> Sort.by(Sort.Direction.DESC, "timestamp");
            case OLDEST -> Sort.by(Sort.Direction.ASC, "timestamp");
        };

        String normalizedSearch = searchQuery == null ? "" : searchQuery.trim();
        boolean searchEnabled = !normalizedSearch.isBlank();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Activity> activities = activityRepository.findAllFiltered(actionType, targetType, searchQuery, searchEnabled, pageable);

        return activities.map(activityMapper::toDTO);
    }

    @Override
    public void createActivity(ActivityRequestDTO activityRequestDTO) {
        User foundUser = userRepository.findById(activityRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Activity activity = Activity.builder()
                .user(foundUser)
                .target(activityRequestDTO.getTarget())
                .targetId(activityRequestDTO.getTargetId())
                .actionType(activityRequestDTO.getActionType())
                .targetType(activityRequestDTO.getTargetType())
                .timestamp(LocalDateTime.now())
                .build();

        activityRepository.save(activity);
    }
}
