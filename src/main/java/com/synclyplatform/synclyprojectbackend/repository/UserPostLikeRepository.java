package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.like.UserPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostLikeRepository extends JpaRepository<UserPostLike, Long> {
    boolean existsByPostIdAndUserUserId(long postId, long userUserId);
}
