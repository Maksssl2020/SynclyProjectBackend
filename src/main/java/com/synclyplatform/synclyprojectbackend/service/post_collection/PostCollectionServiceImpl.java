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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCollectionServiceImpl implements PostCollectionService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCollectionRepository postCollectionRepository;
    private final PostCollectionMapper postCollectionMapper;

    @Override
    public void savePostCollection(Long userId, PostCollectionRequestDTO postCollectionRequest) {
        if (existByNameAndUserId(userId, postCollectionRequest.getTitle())) {
            throw new RuntimeException("Post collection already exists.");
        }

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        PostCollection postCollection = PostCollection.builder()
                .title(postCollectionRequest.getTitle())
                .color(postCollectionRequest.getColor())
                .user(foundUser)
                .build();

        PostCollection savedPostCollection = postCollectionRepository.save(postCollection);
        foundUser.getPostCollections().add(savedPostCollection);
        userRepository.save(foundUser);
    }

    @Override
    public void savePostInCollection(Long postId, Long postCollectionId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        PostCollection founPostCollection = postCollectionRepository.findById(postCollectionId)
                .orElseThrow(() -> new RuntimeException("Post collection not found"));
        PostCollection defaultPostCollection = postCollectionRepository.findByUserUserIdAndTitle(founPostCollection.getUser().getUserId(), "ALL")
                .orElse(null);
        User user = founPostCollection.getUser();

        founPostCollection.getPosts().add(foundPost);

        if (defaultPostCollection != null) {
            defaultPostCollection.getPosts().add(foundPost);
            postCollectionRepository.save(defaultPostCollection);
        }

        postCollectionRepository.save(founPostCollection);
        foundPost.getSavedByUsers().add(user);
        user.getSavedPosts().add(foundPost);
        userRepository.save(user);
        postRepository.save(foundPost);
    }

    @Override
    public void unsavePostFromCollection(Long postId, Long userId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        PostCollection founPostCollection = postCollectionRepository.findByUserUserIdAndPostsContaining(userId, foundPost)
                .orElseThrow(() -> new RuntimeException("Post collection not found"));
        PostCollection defaultPostCollection = postCollectionRepository.findByUserUserIdAndTitle(founPostCollection.getUser().getUserId(), "ALL")
                .orElse(null);
        User user = founPostCollection.getUser();

        founPostCollection.getPosts().remove(foundPost);

        if (defaultPostCollection != null) {
            defaultPostCollection.getPosts().remove(foundPost);
            postCollectionRepository.save(defaultPostCollection);
        }

        postCollectionRepository.save(founPostCollection);
        foundPost.getSavedByUsers().remove(user);
        user.getSavedPosts().remove(foundPost);
        userRepository.save(user);
        postRepository.save(foundPost);
    }

    @Override
    public void unsavePostFromCollectionByPostCollectionId(Long postId, Long postCollectionId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        PostCollection foundPostCollection = postCollectionRepository.findByIdAndPostsContaining(postCollectionId, foundPost)
                .orElseThrow(() -> new RuntimeException("Post collection not found"));
        User user = foundPostCollection.getUser();

        PostCollection defaultPostCollection = postCollectionRepository.findByUserUserIdAndTitle(user.getUserId(), "ALL")
                .orElse(null);

        foundPostCollection.getPosts().remove(foundPost);

        if (defaultPostCollection != null) {
            defaultPostCollection.getPosts().remove(foundPost);
            postCollectionRepository.save(defaultPostCollection);
        }

        postCollectionRepository.save(foundPostCollection);
        foundPost.getSavedByUsers().remove(user);
        user.getSavedPosts().remove(foundPost);
        userRepository.save(user);
        postRepository.save(foundPost);
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
