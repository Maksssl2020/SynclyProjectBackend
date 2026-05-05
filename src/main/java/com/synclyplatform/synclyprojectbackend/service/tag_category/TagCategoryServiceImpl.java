package com.synclyplatform.synclyprojectbackend.service.tag_category;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag_category.TagCategoryDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag_category.TagCategoryRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import com.synclyplatform.synclyprojectbackend.model.tag_category.TagCategory;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.TagCategoryRepository;
import com.synclyplatform.synclyprojectbackend.repository.TagRepository;
import com.synclyplatform.synclyprojectbackend.service.activity.ActivityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagCategoryServiceImpl implements TagCategoryService {

    private final TagRepository tagRepository;
    private final TagCategoryRepository tagCategoryRepository;
    private final ActivityService activityService;

    @Override
    @Transactional
    public void save(User adminUser, TagCategoryRequestDTO tagCategoryRequestDTO) {
        if (!tagCategoryExists(tagCategoryRequestDTO.getName())) {
            TagCategory tagCategory = TagCategory.builder()
                    .name(tagCategoryRequestDTO.getName())
                    .description(tagCategoryRequestDTO.getDescription())
                    .color(tagCategoryRequestDTO.getColor())
                    .build();

            tagCategoryRepository.save(tagCategory);

            activityService.createActivity(
                    ActivityRequestDTO.builder()
                            .userId(adminUser.getUserId())
                            .targetId(tagCategory.getId())
                            .targetType(ActivityTargetType.TAG_CATEGORY)
                            .actionType(ActivityActionType.CREATED)
                            .target(tagCategory.getName())
                            .build()
            );
        }
    }

    @Override
    public boolean tagCategoryExists(String name) {
        return tagCategoryRepository.countByName(name) > 0;
    }

    @Override
    public TagCategoryDTO findById(Long id) {
        TagCategory tagCategory = tagCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag category with id " + id + " doesn't exist"));

        return toDTO(tagCategory);
    }

    @Override
    public List<TagCategoryDTO> findAll() {
        return tagCategoryRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TagCategoryDTO toDTO(TagCategory tagCategory) {
        return TagCategoryDTO.builder()
                .id(tagCategory.getId())
                .name(tagCategory.getName())
                .description(tagCategory.getDescription())
                .color(tagCategory.getColor())
                .tagCount(tagRepository.countByTagCategoryName(tagCategory.getName()))
                .build();
    }
}
