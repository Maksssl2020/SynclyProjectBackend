package com.synclyplatform.synclyprojectbackend.service.tag;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.*;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.tag.TagType;
import com.synclyplatform.synclyprojectbackend.model.tag_category.TagCategory;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.TagCategoryRepository;
import com.synclyplatform.synclyprojectbackend.repository.TagRepository;
import com.synclyplatform.synclyprojectbackend.mapper.TagMapper;
import com.synclyplatform.synclyprojectbackend.service.activity.ActivityService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagCategoryRepository tagCategoryRepository;
    private final ActivityService activityService;
    private final TagMapper tagMapper;

    @Override
    @Transactional
    public void saveMainTag(User adminUser, AdminTagRequestDTO mainTagRequest) {
        if (!tagExists(mainTagRequest.getName())) {
            TagCategory tagCategory = tagCategoryRepository.findByName(mainTagRequest.getTagCategoryName())
                    .orElseThrow(() -> new RuntimeException(String.format("Tag category %s name not found.",  mainTagRequest.getTagCategoryName())));

            Tag tag = Tag.builder()
                    .name(mainTagRequest.getName())
                    .tagCategory(tagCategory)
                    .color(mainTagRequest.getColor())
                    .type(TagType.MAIN)
                    .build();

            tagRepository.save(tag);

            activityService.createActivity(
                    ActivityRequestDTO.builder()
                            .userId(adminUser.getUserId())
                            .targetId(tag.getId())
                            .target(tag.getName())
                            .actionType(ActivityActionType.CREATED)
                            .targetType(ActivityTargetType.TAG)
                            .build()
            );
        }
    }

    @Override
    public TagDTO saveCommonTag(CommonTagRequestDTO commonTagRequest) {
        return tagMapper.toDTO(createCommonTag(commonTagRequest.getName()));
    }

    @Override
    public Tag saveCommonTag(String tagName) {
        return createCommonTag(tagName);
    }

    private Tag createCommonTag(String tagName) {
        if (!tagExists(tagName)) {
            TagCategory common = tagCategoryRepository.findByName("COMMON")
                    .orElseThrow(() -> new EntityNotFoundException("Tag category name not found."));

            Tag tag = Tag.builder()
                    .name(tagName)
                    .type(TagType.COMMON)
                    .color("#8b5cf6")
                    .tagCategory(common)
                    .build();

            return tagRepository.save(tag);
        } else {
            throw new EntityExistsException("Tag exists.");
        }
    }

    @Override
    public TagDTO getTagByName(String tagName) {
        Tag foundTag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found."));

        Integer postsThisWeekCount = tagRepository.countPostsByTagIdCreatedThisWeek(foundTag.getId());

        TagDTO mappedTag = tagMapper.toDTO(foundTag);
        mappedTag.setPostsThisWeek(postsThisWeekCount);

        return mappedTag;
    }

    @Override
    public List<TagDTO> findAllTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDTO> findRelatedTagsByCategory(String category) {
        return tagRepository.findAllByTagCategoryName(category).stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagUsageDTO> findPopularTags(int limit) {
        return tagRepository.findPopularTags(limit);
    }

    @Override
    public List<TagUsageDTO> findTrendingTags(int limit) {
        return tagRepository.findTrendingTagsThisWeek(limit);
    }

    @Override
    public List<TagDTO> searchTags(String query) {
        return tagRepository.searchTagByQuery(query).stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean tagExists(String tagName) {
        return tagRepository.countByName(tagName) > 0;
    }

    @Override
    @Transactional
    public void updateTrendingTags() {
        List<Long> trendingTagsIds = tagRepository.findTrendingTagsIds(6);

        tagRepository.clearTrendingTags();

        if (!trendingTagsIds.isEmpty()) {
            tagRepository.markTagsAsTrending(trendingTagsIds);
        }
    }

    @Override
    @Transactional
    public void changeTagCategory(User adminUser, ChangeTagCategoryRequestDTO changeTagCategoryRequestDTO) {
        Tag foundTag = tagRepository.findById(changeTagCategoryRequestDTO.getTagId())
                .orElseThrow(() -> new EntityNotFoundException("Tag not found."));

        TagCategory foundTagCategory = tagCategoryRepository.findById(changeTagCategoryRequestDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Tag category not found."));

        if (foundTag.getTagCategory().getId().equals(foundTagCategory.getId())) {
            return;
        }

        foundTag.setTagCategory(foundTagCategory);

        activityService.createActivity(
                ActivityRequestDTO.builder()
                        .target(foundTag.getName())
                        .userId(adminUser.getUserId())
                        .targetId(foundTag.getId())
                        .targetType(ActivityTargetType.TAG)
                        .actionType(ActivityActionType.CHANGED_CATEGORY)
                        .build()
        );

        tagRepository.save(foundTag);
    }
}
