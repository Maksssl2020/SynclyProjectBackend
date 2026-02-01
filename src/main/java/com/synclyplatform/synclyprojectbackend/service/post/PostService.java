package com.synclyplatform.synclyprojectbackend.service.post;

import com.synclyplatform.synclyprojectbackend.dto.post.PostDTO;
import com.synclyplatform.synclyprojectbackend.dto.post.PostRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.post.UpdatePostRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {

    void save(Long userId, PostRequestDTO postRequestDTO);
    List<PostDTO> getPostsByUserId(Long userId);
    List<PostDTO> searchPostsByQuery(String query);
    List<PostDTO> getForYouFeed(Long userId, int offset, int limit);
    List<PostDTO> getFollowedFeed(Long userId, int offset, int limit);
    List<PostDTO> getPostsByTag(String tag, int offset, int limit);
    void deletePost(Long postId);

    void update(UpdatePostRequestDTO updatePostRequestDTO);
}
