package com.synclyplatform.synclyprojectbackend.service.tag;

import com.synclyplatform.synclyprojectbackend.dto.tag.CommonTagRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.MainTagRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.TagUsageDTO;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.tag.TagType;
import com.synclyplatform.synclyprojectbackend.model.tag_category.TagCategory;
import com.synclyplatform.synclyprojectbackend.repository.TagCategoryRepository;
import com.synclyplatform.synclyprojectbackend.repository.TagRepository;
import com.synclyplatform.synclyprojectbackend.mapper.TagMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagCategoryRepository tagCategoryRepository;
    private final TagMapper tagMapper;

    @Override
    public void saveMainTag(MainTagRequestDTO mainTagRequest) {
        if (!tagExists(mainTagRequest.getName())) {
            TagCategory tagCategory = tagCategoryRepository.findByName(mainTagRequest.getTagCategoryName())
                    .orElseThrow(() -> new RuntimeException(String.format("Tag category %s name not found.",  mainTagRequest.getTagCategoryName())));

            Tag tag = Tag.builder()
                    .name(mainTagRequest.getName())
                    .description(mainTagRequest.getDescription())
                    .trending(mainTagRequest.isTrending())
                    .tagCategory(tagCategory)
                    .color(mainTagRequest.getColor())
                    .type(TagType.MAIN)
                    .build();

            tagRepository.save(tag);
        }
    }

    @Override
    public void saveCommonTag(CommonTagRequestDTO commonTagRequest) {
        if (!tagExists(commonTagRequest.getName())) {
            Tag tag = Tag.builder()
                    .name(commonTagRequest.getName())
                    .type(TagType.COMMON)
                    .build();

            tagRepository.save(tag);
        }
    }

    @Override
    public TagDTO getTagByName(String tagName) {
        Tag foundTag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found."));

        return tagMapper.toDTO(foundTag);
    }

    @Override
    public List<TagDTO> findAllTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDTO> findRelatedTagsByCategory(String category) {
        return tagRepository.findAllByTagCategoryName(category).stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagUsageDTO> findPopularTags(int limit) {
        return tagRepository.findPopularTags(limit);
    }

    @Override
    public List<TagUsageDTO> findTrendingTags(int limit) {
        return tagRepository.findTrendingTags(limit);
    }

    @Override
    public List<TagDTO> searchTags(String query) {
        return tagRepository.searchTagByQuery(query).stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean tagExists(String tagName) {
        return tagRepository.countByName(tagName) > 0;
    }
}
