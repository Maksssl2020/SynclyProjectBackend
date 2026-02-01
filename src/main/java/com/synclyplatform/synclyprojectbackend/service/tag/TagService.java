package com.synclyplatform.synclyprojectbackend.service.tag;

import com.synclyplatform.synclyprojectbackend.dto.tag.CommonTagRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.MainTagRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.TagUsageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {

    void saveMainTag(MainTagRequestDTO mainTagRequest);
    void saveCommonTag(CommonTagRequestDTO commonTagRequest);
    TagDTO getTagByName(String tagName);
    List<TagDTO> findAllTags();
    List<TagDTO> findRelatedTagsByCategory(String category);
    List<TagUsageDTO> findPopularTags(int limit);
    List<TagUsageDTO> findTrendingTags(int limit);
    List<TagDTO> searchTags(String query);
    boolean tagExists(String tagName);
}
