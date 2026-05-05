package com.synclyplatform.synclyprojectbackend.service.tag;

import com.synclyplatform.synclyprojectbackend.dto.tag.*;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {

    void saveMainTag(User adminUser, AdminTagRequestDTO mainTagRequest);
    TagDTO saveCommonTag(CommonTagRequestDTO commonTagRequest);
    Tag saveCommonTag(String tagName);
    TagDTO getTagByName(String tagName);
    List<TagDTO> findAllTags();
    List<TagDTO> findRelatedTagsByCategory(String category);
    List<TagUsageDTO> findPopularTags(int limit);
    List<TagUsageDTO> findTrendingTags(int limit);
    List<TagDTO> searchTags(String query);
    boolean tagExists(String tagName);
    void updateTrendingTags();

    void changeTagCategory(User adminUser, ChangeTagCategoryRequestDTO changeTagCategoryRequestDTO);
}
