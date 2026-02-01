package com.synclyplatform.synclyprojectbackend.service.shared_post;

import com.synclyplatform.synclyprojectbackend.dto.shared_post.SharedPostDTO;
import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.shared_post.SharedPost;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.SharedPostRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.mapper.SharedPostMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedPostServiceImpl implements SharedPostService {

    private final PostRepository postRepository;
    private final SharedPostRepository sharedPostRepository;
    private final UserRepository userRepository;
    private final SharedPostMapper sharedPostMapper;

    @Override
    public List<SharedPostDTO> findAllByUser(User user) {
        return sharedPostRepository.findBySharedBy(user).stream()
                .map(sharedPostMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SharedPostDTO> findAllByUserId(Long userId) {
        return sharedPostRepository.findBySharedByUserId(userId).stream()
                .map(sharedPostMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void sharePost(User user, Long postId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post with id " + postId + " not found"));

        SharedPost sharedPost = SharedPost.builder()
                .originalPost(foundPost)
                .sharedBy(user)
                .sharedAt(LocalDateTime.now())
                .build();

        sharedPostRepository.save(sharedPost);
    }

    @Override
    public void sharePost(Long userId, Long postId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        sharePost(foundUser, postId);
    }

    @Override
    public void unsharePost(User user, Long postId) {
        SharedPost foundSharedPost = sharedPostRepository.findBySharedByAndOriginalPostId(user, postId)
                .orElseThrow(() -> new EntityNotFoundException("There is no shared post."));

        sharedPostRepository.delete(foundSharedPost);
    }

    @Override
    public void unsharePost(Long userId, Long postId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        unsharePost(foundUser, postId);
    }
}
