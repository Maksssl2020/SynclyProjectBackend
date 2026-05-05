package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByAuthorUserId(Long userId);

    List<Post> findAllByTagsContaining(Tag tag, Pageable pageable);

    Long countByAuthorUserId(Long userId);

    @Query("SELECT p FROM Post p WHERE p.author.userId != :userId ORDER BY RANDOM()")
    List<Post> findRandomPostsExcludingUser(@Param("userId") Long userId, @Param("limit") int limit);

    @Query("""
        SELECT p FROM Post p 
        WHERE p.author.userId != :userId 
        AND p.id >= :startId
        ORDER BY p.id ASC
    """)
    List<Post> findRandomPostsFromId(
            @Param("userId") Long userId,
            @Param("startId") Long startId,
            Pageable pageable
    );

    @Query("""
        SELECT DISTINCT p FROM Post p
        LEFT JOIN p.tags t
        WHERE
            LOWER(p.author.username) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(p.author.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(p.author.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(CONCAT(p.author.firstName, ' ', p.author.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(CONCAT(p.author.lastName, ' ', p.author.firstName)) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Post> searchPostsByQuery(@Param("query") String query);

    List<Post> findByAuthorInOrTagsIn(Set<User> users, Set<Tag> tags);

    @Query("""
        SELECT p FROM Post p
        LEFT JOIN p.tags t
        WHERE (p.author.userId IN :userIds OR t.id IN :tagIds)
        ORDER BY p.createdAt DESC
    """)
    List<Post> findPostsByAuthorsOrTags(
            @Param("userIds") Set<Long> userIds,
            @Param("tagIds") Set<Long> tagIds,
            Pageable pageable
    );


    @Query("SELECT MIN(p.id) FROM Post p")
    Long findMinPostId();

    @Query("SELECT MAX(p.id) FROM Post p")
    Long findMaxPostId();

    Long countAllByCreatedAtBetween(LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);
}
