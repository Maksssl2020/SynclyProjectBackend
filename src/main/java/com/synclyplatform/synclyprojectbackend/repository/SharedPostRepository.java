package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.dto.shared_post.SharedPostDTO;
import com.synclyplatform.synclyprojectbackend.model.shared_post.SharedPost;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharedPostRepository extends JpaRepository<SharedPost,Long> {

    List<SharedPost> findBySharedByUserId(Long sharedByUserId);
    List<SharedPost> findBySharedBy(User sharedBy);
    Optional<SharedPost> findBySharedByAndOriginalPostId(User sharedBy, Long originalPostId);
}
