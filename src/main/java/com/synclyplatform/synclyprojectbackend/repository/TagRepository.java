package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.tag_category.TagCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Long countByTagCategoryName(String tagCategoryName);
    Long countByName(String tagName);
}
