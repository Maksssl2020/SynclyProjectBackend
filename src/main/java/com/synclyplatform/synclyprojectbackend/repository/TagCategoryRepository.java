package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.tag_category.TagCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagCategoryRepository extends JpaRepository<TagCategory, Long> {

    Optional<TagCategory> findByName(String name);
    Long countByName(String name);
}
