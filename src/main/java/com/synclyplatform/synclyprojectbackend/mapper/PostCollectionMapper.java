package com.synclyplatform.synclyprojectbackend.mapper;

import com.synclyplatform.synclyprojectbackend.dto.post.PostDTO;
import com.synclyplatform.synclyprojectbackend.dto.post_collection.PostCollectionDTO;
import com.synclyplatform.synclyprojectbackend.model.post_collection.PostCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostCollectionMapper {

    private final PostMapper postMapper;

    public PostCollectionDTO toDTO(PostCollection postCollection) {
        List<PostDTO> mappedPosts = postCollection.getPosts().stream().map(postMapper::toDto).toList();
        boolean isDefault = postCollection.isDefault() || postCollection.getTitle().equalsIgnoreCase("ALL");

        return PostCollectionDTO.builder()
                .id(postCollection.getId())
                .title(postCollection.getTitle())
                .color(postCollection.getColor())
                .posts(mappedPosts)
                .isDefault(isDefault)
                .userId(postCollection.getUser().getUserId())
                .build();
    }
}
