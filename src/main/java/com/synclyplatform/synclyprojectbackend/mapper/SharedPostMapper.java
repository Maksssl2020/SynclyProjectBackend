package com.synclyplatform.synclyprojectbackend.mapper;

import com.synclyplatform.synclyprojectbackend.dto.shared_post.SharedPostDTO;
import com.synclyplatform.synclyprojectbackend.model.shared_post.SharedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SharedPostMapper {

    private final PostMapper postMapper;
    private final  UserMapper userMapper;

    public SharedPostDTO toDTO(SharedPost sharedPost) {
        return SharedPostDTO.builder()
                .id(sharedPost.getId())
                .sharedBy(userMapper.toDTO(sharedPost.getSharedBy()))
                .originalPost(postMapper.toDto(sharedPost.getOriginalPost()))
                .sharedAt(sharedPost.getSharedAt().toString())
                .build();
    }
}
