package com.synclyplatform.synclyprojectbackend.service.post_collection;

import com.synclyplatform.synclyprojectbackend.dto.post_collection.PostCollectionDTO;
import com.synclyplatform.synclyprojectbackend.dto.post_collection.PostCollectionRequestDTO;
import com.synclyplatform.synclyprojectbackend.mapper.PostCollectionMapper;
import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.post_collection.PostCollection;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.PostCollectionRepository;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCollectionServiceImpl implements PostCollectionService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCollectionRepository postCollectionRepository;
    private final PostCollectionMapper postCollectionMapper;
    private static final String DEFAULT_COLLECTION_TITLE = "ALL";

    @Override
    @Transactional
    public PostCollectionDTO savePostCollection(Long userId, PostCollectionRequestDTO postCollectionRequest) {
        String title = postCollectionRequest.getTitle().trim();

        if (existByNameAndUserId(userId, title)) {
            throw new RuntimeException("Post collection already exists.");
        }

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        PostCollection postCollection = PostCollection.builder()
                .title(title)
                .color(postCollectionRequest.getColor())
                .user(foundUser)
                .build();

        PostCollection savedPostCollection = postCollectionRepository.save(postCollection);

        return postCollectionMapper.toDTO(savedPostCollection);
    }

    @Override
    @Transactional
    public void savePostInCollection(Long postId, Long postCollectionId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostCollection foundPostCollection = postCollectionRepository.findById(postCollectionId)
                .orElseThrow(() -> new RuntimeException("Post collection not found"));

        User user = foundPostCollection.getUser();

        foundPostCollection.getPosts().add(foundPost);

        PostCollection defaultPostCollection = postCollectionRepository
                .findByUserUserIdAndTitle(user.getUserId(), DEFAULT_COLLECTION_TITLE)
                .orElse(null);

        if (defaultPostCollection != null && !defaultPostCollection.getPosts().contains(foundPost)) {
            defaultPostCollection.getPosts().add(foundPost);
            postCollectionRepository.save(defaultPostCollection);
        }

        foundPost.getSavedByUsers().add(user);
        user.getSavedPosts().add(foundPost);

        postCollectionRepository.save(foundPostCollection);
        userRepository.save(user);
        postRepository.save(foundPost);
    }

    @Override
    @Transactional
    public void unsavePostFromCollectionByPostCollectionId(Long postId, Long postCollectionId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        PostCollection foundPostCollection = postCollectionRepository.findByIdAndPostsContaining(postCollectionId, foundPost)
                .orElseThrow(() -> new RuntimeException("Post collection not found"));
        User user = foundPostCollection.getUser();

        foundPostCollection.getPosts().remove(foundPost);
        postCollectionRepository.save(foundPostCollection);

        boolean stillSavedInOtherCollections = postCollectionRepository
                .existsByUserUserIdAndPostsContainingAndIdNotAndTitleNot(
                        user.getUserId(),
                        foundPost,
                        foundPostCollection.getId(),
                        DEFAULT_COLLECTION_TITLE
                );

        if (!stillSavedInOtherCollections) {
            PostCollection defaultPostCollection = postCollectionRepository
                    .findByUserUserIdAndTitle(user.getUserId(), DEFAULT_COLLECTION_TITLE)
                    .orElse(null);

            if (defaultPostCollection != null) {
                defaultPostCollection.getPosts().remove(foundPost);
                postCollectionRepository.save(defaultPostCollection);
            }

            foundPost.getSavedByUsers().remove(user);
            user.getSavedPosts().remove(foundPost);

            userRepository.save(user);
            postRepository.save(foundPost);
        }
    }

    @Override
    public PostCollectionDTO getPostCollectionById(Long postCollectionId) {
        PostCollection foundPostCollection = postCollectionRepository.findById(postCollectionId)
                .orElseThrow(() -> new RuntimeException("Post collection not found."));

        return postCollectionMapper.toDTO(foundPostCollection);
    }

    @Override
    public List<PostCollectionDTO> getPostCollectionByUserId(Long userId) {
        return postCollectionRepository.findAllByUserUserId(userId).stream()
                .map(postCollectionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existByNameAndUserId(Long userId, String postCollectionName) {
        return postCollectionRepository.existsByUserUserIdAndTitle(userId, postCollectionName);
    }
}
