package com.synclyplatform.synclyprojectbackend.service.post;

import com.synclyplatform.synclyprojectbackend.dto.post.PostDTO;
import com.synclyplatform.synclyprojectbackend.dto.post.PostRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.post.UpdatePostRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.utils.TimestampSortOption;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {

    void save(Long userId, PostRequestDTO postRequestDTO);
    List<PostDTO> getPostsByUserId(Long userId);
    List<PostDTO> searchPostsByQuery(String query);
    List<PostDTO> getForYouFeed(Long userId, int offset, int limit);
    List<PostDTO> getFollowedFeed(Long userId, int offset, int limit);
    List<PostDTO> getPostsByTag(String tag, int offset, int limit, TimestampSortOption sortOption);
    void deletePost(User user, Long postId);

    void update(UpdatePostRequestDTO updatePostRequestDTO);
}
