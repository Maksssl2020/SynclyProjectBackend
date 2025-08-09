package com.synclyplatform.synclyprojectbackend.service.post;

import com.synclyplatform.synclyprojectbackend.dto.post.*;
import com.synclyplatform.synclyprojectbackend.model.post.*;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.media.MediaService;
import com.synclyplatform.synclyprojectbackend.utils.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final MediaService mediaService;

    @Override
    public void save(Long userId, PostRequestDTO postRequestDTO) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println(postRequestDTO.toString());

        switch (postRequestDTO) {
            case TextPostRequestDTO textPostRequestDTO -> {
                TextPost mappedTextPost = postMapper.fromRequestDto(textPostRequestDTO);

                mappedTextPost.setAuthor(foundUser);
                mappedTextPost.setPostType(PostType.TEXT);
                postRepository.save(mappedTextPost);
            }
            case QuotePostRequestDTO quotePostRequestDTO -> {
                QuotePost mappedQuotePost = postMapper.fromRequestDto(quotePostRequestDTO);

                mappedQuotePost.setAuthor(foundUser);
                mappedQuotePost.setPostType(PostType.QUOTE);
                postRepository.save(mappedQuotePost);
            }
            case PhotoPostRequestDTO photoPostRequestDTO -> {
                PhotoPost mappedPhotoPost = postMapper.fromRequestDto(photoPostRequestDTO);

                mappedPhotoPost.setAuthor(foundUser);
                mediaService.savePostPhotos(mappedPhotoPost, photoPostRequestDTO.getPhotos());
                mappedPhotoPost.setPostType(PostType.PHOTO);
                postRepository.save(mappedPhotoPost);
            }
            case VideoPostRequestDTO videoPostRequestDTO -> {
                VideoPost mappedVideoPost = postMapper.fromRequestDto(videoPostRequestDTO);

                mappedVideoPost.setAuthor(foundUser);
                mediaService.savePostVideos(mappedVideoPost, videoPostRequestDTO.getVideos());
                mappedVideoPost.setPostType(PostType.VIDEO);
                postRepository.save(mappedVideoPost);
            }
            case LinkPostRequestDTO linkPostRequestDTO -> {
                LinkPost mappedLinkPost = postMapper.fromRequestDto(linkPostRequestDTO);

                mappedLinkPost.setAuthor(foundUser);
                mappedLinkPost.setPostType(PostType.LINK);
                postRepository.save(mappedLinkPost);
            }
            case null, default ->
                    throw new IllegalArgumentException("Unsupported post type." );
        }
    }

    @Override
    public List<PostDTO> getPostsByUserId(Long userId) {
        return postRepository.findAllByAuthorUserId(userId).stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> searchPostsByQuery(String query) {
        return postRepository.searchPostsByQuery(query).stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getForYouFeed(Long userId, int offset, int limit) {
        Long minId = postRepository.findMinPostId();
        Long maxId = postRepository.findMaxPostId();

        if (minId == null || maxId == null) return List.of();

        Long randomStartId = ThreadLocalRandom.current().nextLong(minId, maxId + 1);
        Pageable pageable = PageRequest.of(offset, limit);

        List<Post> posts = postRepository.findRandomPostsFromId(userId, randomStartId, pageable);

        if (posts.size() < limit) {
            List<Post> fallback = postRepository.findRandomPostsFromId(userId, minId, pageable);
            posts.addAll(fallback.subList(0, Math.min(limit - posts.size(), fallback.size())));
        }

        return posts.stream().map(postMapper::toDto).toList();
    }

    @Override
    public List<PostDTO> getFollowedFeed(Long userId, int offset, int limit) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Long> followedUsersIds = new HashSet<>(foundUser.getFollowedUsers().stream().map(UserProfile::getUser).map(User::getUserId).toList());
        Set<Long> followedTagsIds = new HashSet<>(foundUser.getFollowedTags().stream().map(Tag::getId).toList());

        if (followedUsersIds.isEmpty() && followedTagsIds.isEmpty()) {
            return Collections.emptyList();
        }

        return postRepository.findPostsByAuthorsOrTags(followedUsersIds, followedTagsIds, PageRequest.of(offset, limit))
                .stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

}
