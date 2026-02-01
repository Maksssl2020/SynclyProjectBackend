package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.like.UserCommentLike;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCommentLikeRepository extends JpaRepository<UserCommentLike, Long> {
    Optional<UserCommentLike> findByUserAndPostCommentId(User user, Long postCommentId);
    boolean existsByPostCommentIdAndUserUserId(Long postCommentId, Long userUserId);
}
