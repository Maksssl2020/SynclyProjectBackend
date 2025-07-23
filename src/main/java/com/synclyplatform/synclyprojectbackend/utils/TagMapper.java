package com.synclyplatform.synclyprojectbackend.utils;

import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagMapper {
    private final TagRepository tagRepository;

    public TagDTO toDTO(Tag tag) {
        Long postsCount = tagRepository.countPostsByTagId(tag.getId());

        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .description(tag.getDescription())
                .trending(tag.isTrending())
                .postsCount(postsCount)
                .followersCount(0)
                .type(tag.getType().toString())
                .tagCategory(tag.getTagCategory().getName())
                .color(tag.getColor())
                .createdAt(tag.getCreatedAt().toString())
                .build();
    }
}
