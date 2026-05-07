package com.synclyplatform.synclyprojectbackend.service.tag_category;

import com.synclyplatform.synclyprojectbackend.dto.tag_category.TagCategoryDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag_category.TagCategoryRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagCategoryService {

    TagCategoryDTO findById(Long id);
    List<TagCategoryDTO> findAll();
    void save(User adminUser, TagCategoryRequestDTO tagCategoryRequestDTO);
    boolean tagCategoryExists(String name);

    List<String> getTagsCategoriesNames();
}
