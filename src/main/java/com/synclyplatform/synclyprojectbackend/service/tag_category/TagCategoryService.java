package com.synclyplatform.synclyprojectbackend.service.tag_category;

import com.synclyplatform.synclyprojectbackend.dto.tag_category.TagCategoryDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag_category.TagCategoryRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagCategoryService {

    TagCategoryDTO findById(Long id);
    List<TagCategoryDTO> findAll();
    void save(TagCategoryRequestDTO tagCategoryRequestDTO);
    boolean tagCategoryExists(String name);
}
