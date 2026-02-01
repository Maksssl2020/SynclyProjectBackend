package com.synclyplatform.synclyprojectbackend.service.shared_post;

import com.synclyplatform.synclyprojectbackend.dto.shared_post.SharedPostDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SharedPostService {

    List<SharedPostDTO> findAllByUser(User user);
    List<SharedPostDTO> findAllByUserId(Long userId);
    void sharePost(User user, Long postId);
    void sharePost(Long userId, Long postId);
    void unsharePost(User user, Long postId);
    void unsharePost(Long userId, Long postId);
}
