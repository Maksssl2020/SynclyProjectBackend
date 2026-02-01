package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.like.UserUserProfileLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserUserProfileLikeRepository extends JpaRepository<UserUserProfileLike, Long> {

    Long countByUserProfileUserProfileId(Long userProfileId);
    Long countByUserUserId(Long userId);
    boolean existsByUserUserIdAndUserProfileUserProfileId(Long userUserId, Long userProfileUserProfileId);

    @Query("""
        SELECT uupl.userProfile.userProfileId FROM UserUserProfileLike uupl
        WHERE uupl.user.userId = :userId
    """)
    List<Long> findAllLikedProfilesIdsByUserId(Long userId);
}
