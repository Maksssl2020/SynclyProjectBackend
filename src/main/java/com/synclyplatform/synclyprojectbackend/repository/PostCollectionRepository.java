package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.post_collection.PostCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostCollectionRepository extends JpaRepository<PostCollection, Long> {
    List<PostCollection> findAllByUserUserId(Long userId);
    Optional<PostCollection> findByUserUserIdAndTitle(Long userId, String title);

    Optional<PostCollection> findByUserUserIdAndPostsContaining(Long userUserId, Post post);
    boolean existsByUserUserIdAndTitle(Long userId, String title);
}
