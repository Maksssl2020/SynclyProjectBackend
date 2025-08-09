package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.dto.tag.TagUsageDTO;
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

    @Query(value = """
        SELECT t.id, t.name, COUNT(pt.post_id) AS usageCount
        FROM post_tags pt
        JOIN tag t ON pt.tags_id = t.id
        GROUP BY t.id, t.name
        ORDER BY usageCount DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<TagUsageDTO> findPopularTags(@Param("limit") int limit);

    @Query(value = """
        SELECT t.id, t.name, COUNT(pt.post_id) AS usageCount
        FROM post_tags pt
        JOIN post p ON pt.post_id = p.id
        JOIN tag t ON pt.tags_id = t.id
        WHERE p.created_at > CURRENT_DATE - 7 
        GROUP BY t.id, t.name
        ORDER BY usageCount DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<TagUsageDTO> findTrendingTags(@Param("limit") int limit);


    @Query("""
        SELECT t FROM Tag t WHERE
        LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Tag> searchTagByQuery(@Param("query") String query);
}
