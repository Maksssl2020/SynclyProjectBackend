package com.synclyplatform.synclyprojectbackend.utils;

import com.synclyplatform.synclyprojectbackend.dto.post.*;
import com.synclyplatform.synclyprojectbackend.model.post.*;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToNames")
    TextPostDTO toDto(TextPost post);

    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToNames")
    QuotePostDTO toDto(QuotePost post);

    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToNames")
    PhotoPostDTO toDto(PhotoPost post);

    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToNames")
    AudioPostDTO toDto(AudioPost post);

    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToNames")
    VideoPostDTO toDto(VideoPost post);

    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToNames")
    LinkPostDTO toDto(LinkPost post);

    @Named("mapTagsToNames")
    default List<String> mapTagsToNames(List<Tag> tags) {
        if (tags == null) return null;
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
    }
}
