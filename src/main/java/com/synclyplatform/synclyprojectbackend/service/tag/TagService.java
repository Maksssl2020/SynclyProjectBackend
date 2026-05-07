package com.synclyplatform.synclyprojectbackend.service.tag;

import com.synclyplatform.synclyprojectbackend.dto.tag.*;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.utils.TimestampSortOption;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {

    void saveMainTag(User adminUser, AdminTagRequestDTO mainTagRequest);
    TagDTO saveCommonTag(CommonTagRequestDTO commonTagRequest);
    Tag saveCommonTag(String tagName);
    TagDTO getTagByName(String tagName);
    Page<TagDTO> findAllTags(int page, int size, String tagCategoryName, boolean trendingOnly, TimestampSortOption sortOption, String searchQuery);
    List<TagDTO> findRelatedTagsByCategory(String category);
    List<TagUsageDTO> findPopularTags(int limit);
    List<TagUsageDTO> findTrendingTags(int limit);
    List<TagDTO> searchTags(String query);
    List<TagDTO> findAllEnabledTags();
    boolean tagExists(String tagName);
    void updateTrendingTags();
    void changeTagCategory(User adminUser, ChangeTagCategoryRequestDTO changeTagCategoryRequestDTO, boolean createActivity);
    TagStatsAdminDTO getTagAdminStats();

    TagToEditDTO getTagToEditById(Long tagId);

    void updateTag(User adminUser, TagUpdateRequestDTO tagUpdateRequestDTO);

    void disableEnableTagById(User adminUser, Long tagId);
}
