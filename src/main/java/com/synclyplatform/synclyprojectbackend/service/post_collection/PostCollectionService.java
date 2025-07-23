package com.synclyplatform.synclyprojectbackend.service.post_collection;

import com.synclyplatform.synclyprojectbackend.dto.post_collection.PostCollectionDTO;
import com.synclyplatform.synclyprojectbackend.dto.post_collection.PostCollectionRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostCollectionService {

    void savePostCollection(Long userId, PostCollectionRequestDTO postCollectionRequest);
    void savePostInCollection(Long postId, Long postCollectionId);
    void unsavePostFromCollection(Long postId, Long userId);
    PostCollectionDTO getPostCollectionById(Long postCollectionId);
    List<PostCollectionDTO> getPostCollectionByUserId(Long userId);
    boolean existByNameAndUserId(Long userId, String postCollectionName);
}
