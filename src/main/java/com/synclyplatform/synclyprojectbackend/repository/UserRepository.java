package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.userId FROM User u")
    List<Long> findAllUserIds();

    List<User> findAllByStatus(UserStatus status);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("""
        SELECT u FROM User u WHERE
        LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR
        LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR
        LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<User> searchUserByQuery(@Param("query") String query);

    Long countAllByRole(UserRole role);
    Long countAllByRoleAndCreatedAtBetween(UserRole role, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);
    Long countAllByFollowedTagsContaining(Tag followedTag);

    @Query("""
        SELECT u.username FROM User u
    """)
    List<String> findOnlyUsersUsernames();

    @Query("""
        SELECT u.userProfile.displayName FROM User u
    """)
    List<String> findOnlyUsersDisplayNames();

    @Query(value = """
        SELECT * FROM app_user u
        WHERE u.user_id <> :userId
          AND u.user_id NOT IN (:excludedIds)
        ORDER BY RANDOM()
        LIMIT :limit
    """, nativeQuery = true)
    List<User> findRandomUsersExclude(
            @Param("userId") Long userId,
            @Param("excludedIds") Collection<Long> excludedIds,
            @Param("limit") int limit
    );
}
