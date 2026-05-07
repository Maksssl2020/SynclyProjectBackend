package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.dto.user.AdminUserDTO;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
        SELECT COUNT(DISTINCT u) FROM User u
        JOIN u.followedTags
        WHERE SIZE(u.followedTags) > 0
    """)
    long countUsersFollowingAnyTag();

    @Query("""
        SELECT COUNT(u) > 0 FROM User u WHERE
            u.userId = :userId AND
            u.role = 'ADMIN'
    """)
    boolean isAdminByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT COUNT(u) > 0
        FROM User u
        JOIN u.followedUsers fu
            WHERE u.userId = :userId
            AND fu.userProfileId = :userProfileId
    """)
    boolean existsFollowedProfile(@Param("userId") Long userId, @Param("userProfileId") Long userProfileId);

    @Query("""
        SELECT u FROM User u WHERE
            (
                LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(CONCAT(u.lastName, ' ', u.firstName)) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            AND u.role <> 'ADMIN'
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
            AND u.role <> 'ADMIN'
        ORDER BY RANDOM()
        LIMIT :limit
    """, nativeQuery = true)
    List<User> findRandomUsersExclude(
            @Param("userId") Long userId,
            @Param("excludedIds") Collection<Long> excludedIds,
            @Param("limit") int limit
    );

    @Query("""
        SELECT u FROM User u
        WHERE (:userRole IS NULL OR u.role = :userRole)
        AND (:userStatus IS NULL OR u.status = :userStatus)
        AND (
                :searchEnabled = FALSE OR
                LOWER(u.username) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR
                LOWER(u.email) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
            )
    """)
    Page<User> findAllFiltered(UserRole userRole, UserStatus userStatus, String searchQuery, boolean searchEnabled, Pageable pageable);

    @Query("""
        SELECT u
        FROM User u
        WHERE (:userRole IS NULL OR u.role = :userRole)
        AND (:userStatus IS NULL OR u.status = :userStatus)
        AND (
            :searchEnabled = FALSE OR
            LOWER(u.username) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR
            LOWER(u.email) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
        )
    """)
    Page<User> findAllFilteredForAdmin(
            UserRole userRole,
            UserStatus userStatus,
            String searchQuery,
            boolean searchEnabled,
            Pageable pageable
    );

    @Query("""
        SELECT u
        FROM User u
        LEFT JOIN Post p ON p.author = u
        WHERE (:userRole IS NULL OR u.role = :userRole)
        AND (:userStatus IS NULL OR u.status = :userStatus)
        AND (
            :searchEnabled = FALSE OR
            LOWER(u.username) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR
            LOWER(u.email) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
        )
        ORDER BY COUNT (DISTINCT p.id) ASC
    """)
    Page<User> findAllFilteredForAdminAndSortedByPostCountAsc(
            UserRole userRole,
            UserStatus userStatus,
            String searchQuery,
            boolean searchEnabled,
            Pageable pageable
    );

    @Query("""
        SELECT u
        FROM User u
        LEFT JOIN Post p ON p.author = u
        WHERE (:userRole IS NULL OR u.role = :userRole)
        AND (:userStatus IS NULL OR u.status = :userStatus)
        AND (
            :searchEnabled = FALSE OR
            LOWER(u.username) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR
            LOWER(u.email) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
        )
        ORDER BY COUNT (DISTINCT p.id) DESC
    """)
    Page<User> findAllFilteredForAdminAndSortedByPostCountDesc(
            UserRole userRole,
            UserStatus userStatus,
            String searchQuery,
            boolean searchEnabled,
            Pageable pageable
    );

    @Query("""
        SELECT u
        FROM User u
        LEFT JOIN UserProfile up ON up.user = u
        WHERE (:userRole IS NULL OR u.role = :userRole)
        AND (:userStatus IS NULL OR u.status = :userStatus)
        AND (
            :searchEnabled = FALSE OR
            LOWER(u.username) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR
            LOWER(u.email) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
        )
        ORDER BY SIZE(up.followers) ASC
    """)
    Page<User> findAllFilteredForAdminAndSortedByFollowersCountAsc(
            UserRole userRole,
            UserStatus userStatus,
            String searchQuery,
            boolean searchEnabled,
            Pageable pageable
    );

    @Query("""
        SELECT u
        FROM User u
        LEFT JOIN UserProfile up ON up.user = u
        WHERE (:userRole IS NULL OR u.role = :userRole)
        AND (:userStatus IS NULL OR u.status = :userStatus)
        AND (
            :searchEnabled = FALSE OR
            LOWER(u.username) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR
            LOWER(u.email) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
        )
        ORDER BY SIZE(up.followers) DESC
    """)
    Page<User> findAllFilteredForAdminAndSortedByFollowersCountDesc(
            UserRole userRole,
            UserStatus userStatus,
            String searchQuery,
            boolean searchEnabled,
            Pageable pageable
    );
}
