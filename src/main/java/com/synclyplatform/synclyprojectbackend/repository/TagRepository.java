package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.dto.tag.TagUsageDTO;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.tag.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);
    Long countByTagCategoryName(String tagCategoryName);
    Long countByName(String tagName);

    @Query("""
        SELECT t.name FROM Tag t
    """)
    List<String> findOnlyTagNames();

    List<Tag> findAllByTagCategoryName(String tagCategoryName);

    @Query(value = """
        SELECT COUNT(pt.post_id) FROM post_tags pt WHERE tags_id = :tagId
    """,  nativeQuery = true)
    Long countPostsByTagId(@Param("tagId") Long tagId);

    @Query(value = """
        SELECT COUNT(*)
        FROM post_tags pt
        JOIN post p on pt.post_id = p.id
        WHERE pt.tags_id = :tagId
            AND p.created_at >= NOW() - INTERVAL '7 days'
    """, nativeQuery = true)
    Integer countPostsByTagIdCreatedThisWeek(@Param("tagId") Long tagId);

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
    List<TagUsageDTO> findTrendingTagsThisWeek(@Param("limit") int limit);

    @Query(value = """
        SELECT t.id
        FROM tag t
        LEFT JOIN post_tags pt on t.id = pt.tags_id
        LEFT JOIN post p on pt.post_id = p.id
        GROUP BY t.id
        HAVING
                SUM(CASE WHEN p.created_at >= NOW() - INTERVAL '7 days' THEN 1 ELSE 0 END) >
                SUM(CASE WHEN p.created_at >= NOW() - INTERVAL '14 days' AND p.created_at < NOW() - INTERVAL '7 days' THEN 1 ELSE 0 END)
        ORDER BY
                (
                    SUM(CASE WHEN p.created_at >= NOW() - INTERVAL '7 days' THEN 1 ELSE 0 END)
                    -
                    SUM(CASE WHEN p.created_at >= NOW() - INTERVAL '14 days' AND p.created_at < NOW() - INTERVAL '7 days' THEN 1 ELSE 0 END)
                )
        DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Long> findTrendingTagsIds(@Param("limit") int limit);

    @Modifying
    @Query("""
        UPDATE Tag t
        SET t.trending = FALSE
        WHERE t.trending IS TRUE
    """)
    void clearTrendingTags();

    @Modifying
    @Query("""
        UPDATE Tag t
        SET t.trending = TRUE
        WHERE t.id IN :tagsIds
    """)
    void markTagsAsTrending(@Param("tagsIds") List<Long> tagsIds);

    @Query("""
        SELECT t FROM Tag t
        LEFT JOIN t.tagCategory tc
        WHERE
            LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(CAST(t.type AS string)) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(tc.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Tag> searchTagByQuery(@Param("query") String query);

    Long countAllByType(TagType type);

    Long countAllByTypeAndCreatedAtBetween(TagType type, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);
}
