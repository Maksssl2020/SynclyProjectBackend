package com.synclyplatform.synclyprojectbackend.utils;

import com.synclyplatform.synclyprojectbackend.model.like.UserPostLike;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.tag.TagType;
import com.synclyplatform.synclyprojectbackend.repository.PostCommentRepository;
import com.synclyplatform.synclyprojectbackend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostMapperHelper {

    private final TagRepository tagRepository;
    private final PostCommentRepository postCommentRepository;

    public Long countPostComments(Long postId) {
        return postCommentRepository.countByPostId(postId);
    }

    public List<Tag> mapTagNamesToTags(List<String> tagNames) {
        if (tagNames == null) return List.of();

        return tagNames.stream()
                .map(this::findOrCreateTag)
                .collect(Collectors.toList());
    }

    private Tag findOrCreateTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setType(TagType.COMMON);
                    newTag.setName(name);
                    return newTag;
                });
    }


}
