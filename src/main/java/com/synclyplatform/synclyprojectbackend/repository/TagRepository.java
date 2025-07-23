package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.tag_category.TagCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);
    Long countByTagCategoryName(String tagCategoryName);
    Long countByName(String tagName);

    @Query(value = """
        SELECT COUNT(pt.post_id) FROM post_tags pt WHERE tags_id = :tagId
    """,  nativeQuery = true)
    Long countPostsByTagId(@Param("tagId")  Long tagId);

    @Query("""
        SELECT t FROM Tag t WHERE
        LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Tag> searchTagByQuery(@Param("query") String query);
}
