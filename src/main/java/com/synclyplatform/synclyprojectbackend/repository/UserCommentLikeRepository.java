package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.like.UserCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCommentLikeRepository extends JpaRepository<UserCommentLike, Long> {
    boolean existsByPostCommentIdAndUserUserId(Long postCommentId, Long userUserId);
}
