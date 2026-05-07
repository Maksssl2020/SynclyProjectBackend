package com.synclyplatform.synclyprojectbackend.mapper;

import com.synclyplatform.synclyprojectbackend.dto.tag.PostTagDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.TagToEditDTO;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.repository.TagRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagMapper {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public TagDTO toDTO(Tag tag) {
        Long postsCount = tagRepository.countPostsByTagId(tag.getId());
        Long followersCount = userRepository.countAllByFollowedTagsContaining(tag);

        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .trending(tag.isTrending())
                .postsCount(postsCount)
                .followersCount(followersCount)
                .type(tag.getType().toString())
                .tagCategory(tag.getTagCategory().getName())
                .color(tag.getTagCategory().getColor())
                .createdAt(tag.getCreatedAt().toString())
                .enabled(tag.isEnabled())
                .build();
    }

    public PostTagDTO toPostTagDTO(Tag tag) {
        return PostTagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .build();
    }

    public TagToEditDTO toTagToEditDTO(Tag tag) {
        return TagToEditDTO.builder()
                .id(tag.getId())
                .tagCategoryId(tag.getTagCategory().getId())
                .tagCategoryName(tag.getTagCategory().getName())
                .name(tag.getName())
                .color(tag.getColor())
                .build();
    }
}
