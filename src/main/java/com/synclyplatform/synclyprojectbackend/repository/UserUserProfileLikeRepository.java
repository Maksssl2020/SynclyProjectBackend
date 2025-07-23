package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.like.UserUserProfileLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserUserProfileLikeRepository extends JpaRepository<UserUserProfileLike, Long> {

    Long countByUserProfileUserProfileId(Long userProfileId);
    Long countByUserUserId(Long userId);
    boolean existsByUserUserIdAndUserProfileUserProfileId(Long userUserId, Long userProfileUserProfileId);
}
