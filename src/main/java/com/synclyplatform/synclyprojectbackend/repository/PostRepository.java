package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByAuthorUserId(Long userId);
    Long countByAuthorUserId(Long userId);

    @Query("SELECT p FROM Post p WHERE p.author.userId != :userId ORDER BY RANDOM()")
    List<Post> findRandomPostsExcludingUser(@Param("userId") Long userId, @Param("limit") int limit);

    @Query("""
        SELECT p FROM Post p WHERE
        LOWER(p.author.username) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Post> searchPostsByQuery(@Param("query") String query);
}
