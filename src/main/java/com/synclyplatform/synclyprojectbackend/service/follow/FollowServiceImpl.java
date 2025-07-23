package com.synclyplatform.synclyprojectbackend.service.follow;

import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.TagRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.utils.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public List<TagDTO> getFollowedTags(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Set<Tag> followedTagsCopy = new HashSet<>(foundUser.getFollowedTags());

        return followedTagsCopy.stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void followTag(Long userId, Long tagId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Tag foundTag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found!"));

        foundUser.getFollowedTags().add(foundTag);
        userRepository.save(foundUser);
    }

    @Override
    public void unfollowTag(Long userId, Long tagId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Tag foundTag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found!"));

        foundUser.getFollowedTags().remove(foundTag);
        userRepository.save(foundUser);
    }
}
