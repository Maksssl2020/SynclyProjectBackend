package com.synclyplatform.synclyprojectbackend.service.tag;

import com.synclyplatform.synclyprojectbackend.dto.tag.CommonTagRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.MainTagRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {

    void saveMainTag(MainTagRequestDTO mainTagRequest);
    void saveCommonTag(CommonTagRequestDTO commonTagRequest);
    List<TagDTO> findAllTags();
    List<TagDTO> searchTags(String query);
    boolean tagExists(String tagName);
}
