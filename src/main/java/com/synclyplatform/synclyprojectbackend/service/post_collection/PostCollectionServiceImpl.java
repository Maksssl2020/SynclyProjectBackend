package com.synclyplatform.synclyprojectbackend.service.post_collection;

import com.synclyplatform.synclyprojectbackend.dto.post_collection.PostCollectionDTO;
import com.synclyplatform.synclyprojectbackend.dto.post_collection.PostCollectionRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.post_collection.PostCollection;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.PostCollectionRepository;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.utils.PostMapper;
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
    private final PostMapper postMapper;

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
    public PostCollectionDTO getPostCollectionById(Long postCollectionId) {
        PostCollection foundPostCollection = postCollectionRepository.findById(postCollectionId)
                .orElseThrow(() -> new RuntimeException("Post collection not found."));

        return createPostCollectionDTO(foundPostCollection);
    }

    @Override
    public List<PostCollectionDTO> getPostCollectionByUserId(Long userId) {
        return postCollectionRepository.findAllByUserUserId(userId).stream()
                .map(this::createPostCollectionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existByNameAndUserId(Long userId, String postCollectionName) {
        return postCollectionRepository.existsByUserUserIdAndTitle(userId, postCollectionName);
    }

    private PostCollectionDTO createPostCollectionDTO(PostCollection postCollection) {
        return PostCollectionDTO.builder()
                .id(postCollection.getId())
                .title(postCollection.getTitle())
                .color(postCollection.getColor())
                .posts(postCollection.getPosts().stream().map(postMapper::toDto).toList())
                .isDefault(postCollection.isDefault())
                .userId(postCollection.getUser().getUserId())
                .build();
    }
}
