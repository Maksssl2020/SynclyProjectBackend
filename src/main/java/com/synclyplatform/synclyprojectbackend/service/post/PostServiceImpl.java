package com.synclyplatform.synclyprojectbackend.service.post;

import com.synclyplatform.synclyprojectbackend.dto.activity.ActivityRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.post.*;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import com.synclyplatform.synclyprojectbackend.model.post.*;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.model.utils.TimestampSortOption;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.TagRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.activity.ActivityService;
import com.synclyplatform.synclyprojectbackend.service.media.MediaService;
import com.synclyplatform.synclyprojectbackend.mapper.PostMapper;
import com.synclyplatform.synclyprojectbackend.service.tag.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;
    private final MediaService mediaService;
    private final TagService tagService;
    private final ActivityService activityService;

    @Override
    @Transactional
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
                .filter(post -> !Objects.equals(post.getAuthor().getUserId(), userId))
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getPostsByTag(String tag, int offset, int limit, TimestampSortOption sortOption) {
        Tag foundTag = tagRepository.findByName(tag)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        Sort sort = switch (sortOption) {
            case RECENT -> Sort.by(Sort.Direction.DESC, "createdAt");
            case OLDEST -> Sort.by(Sort.Direction.ASC, "createdAt");
        };

        PageRequest pageable = PageRequest.of(offset, limit, sort);

        return postRepository.findAllByTagsContaining(foundTag, pageable).stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePost(User user, Long postId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!Objects.equals(user.getUserId(), foundPost.getAuthor().getUserId()) && (user.getRole().equals(UserRole.ADMIN) || user.getRole().equals(UserRole.MODERATOR))) {
            activityService.createActivity(
                    ActivityRequestDTO.builder()
                            .actionType(ActivityActionType.DELETED)
                            .target(String.format("POST_ID_%d", foundPost.getId()))
                            .targetId(postId)
                            .userId(user.getUserId())
                            .targetType(ActivityTargetType.POST)
                            .build()
            );
        }

        postRepository.delete(foundPost);
    }

    @Override
    @Transactional
    public void update(UpdatePostRequestDTO updatePostRequestDTO) {
        Post post = postRepository.findById(updatePostRequestDTO.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        PostRequestDTO updatedData = updatePostRequestDTO.getUpdatedData();
        Post postToSave = null;

        switch (updatedData) {
            case TextPostRequestDTO textPostRequestDTO-> {
                if (post instanceof TextPost textPost) {
                    TextPost updatedTextPost = updateTextPost(textPost, textPostRequestDTO);
                    postToSave = compareAndUpdateTags(updatedTextPost, textPostRequestDTO.getTags());
                }
            }
            case QuotePostRequestDTO quotePostRequestDTO -> {
                if (post instanceof QuotePost quotePost) {
                    QuotePost updatedQuotePost = updateQuotePost(quotePost, quotePostRequestDTO);
                    postToSave = compareAndUpdateTags(updatedQuotePost, quotePostRequestDTO.getTags());
                }
            }
            case PhotoPostRequestDTO photoPostRequestDTO -> {
                if (post instanceof PhotoPost photoPost) {
                    PhotoPost updatedPhotoPost = updatePhotoPost(photoPost, photoPostRequestDTO);
                    postToSave = compareAndUpdateTags(updatedPhotoPost, photoPostRequestDTO.getTags());
                }
            }
            case VideoPostRequestDTO videoPostRequestDTO -> {
                if (post instanceof VideoPost videoPost) {
                    VideoPost updatedVideoPost = updateVideoPost(videoPost, videoPostRequestDTO);
                    postToSave = compareAndUpdateTags(updatedVideoPost, videoPostRequestDTO.getTags());
                }
            }
            case LinkPostRequestDTO linkPostRequestDTO -> {
                if (post instanceof LinkPost linkPost) {
                    LinkPost updatedLinkPost = updateLinkPost(linkPost, linkPostRequestDTO);
                    postToSave =  compareAndUpdateTags(updatedLinkPost, linkPostRequestDTO.getTags());
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + updatedData);
        }


        if (postToSave != null) {
            postRepository.save(postToSave);
        }
    }

    @Transactional
    protected Post compareAndUpdateTags(Post post, Set<String> tags) {
        Set<String> mappedTags = post.getTags().stream().map(Tag::getName).collect(Collectors.toSet());

        if (tags != null && !tags.equals(mappedTags)) {
            List<Tag> updatedTags = tags.stream()
                    .map(this::findOrCreateTag)
                    .toList();

            post.getTags().clear();
            post.getTags().addAll(updatedTags);
        }

        return post;
    }

    @Transactional
    protected TextPost updateTextPost(TextPost textPost, TextPostRequestDTO textPostRequestDTO) {
        if (textPostRequestDTO.getTitle() != null) {
            textPost.setTitle(textPostRequestDTO.getTitle());
        }
        if (textPostRequestDTO.getContent() != null) {
            textPost.setContent(textPostRequestDTO.getContent());
        }

        return textPost;
    }

    @Transactional
    protected QuotePost updateQuotePost(QuotePost quotePost, QuotePostRequestDTO quotePostRequestDTO) {
        if (quotePostRequestDTO.getQuote() != null) {
            quotePost.setQuote(quotePostRequestDTO.getQuote());
        }
        if (quotePostRequestDTO.getSource() != null) {
            quotePost.setSource(quotePostRequestDTO.getSource());
        }

        return quotePost;
    }

    @Transactional
    protected PhotoPost updatePhotoPost(PhotoPost photoPost, PhotoPostRequestDTO photoPostRequestDTO) {
        if (photoPostRequestDTO.getCaption() != null) {
            photoPost.setCaption(photoPostRequestDTO.getCaption());
        }
        if (photoPostRequestDTO.getPhotos() != null) {
            mediaService.savePostPhotos(photoPost, photoPostRequestDTO.getPhotos());
        }

        return photoPost;
    }

    @Transactional
    protected VideoPost updateVideoPost(VideoPost videoPost, VideoPostRequestDTO videoPostRequestDTO) {
        if (videoPostRequestDTO.getDescription() != null) {
            videoPost.setDescription(videoPostRequestDTO.getDescription());
        }
        if (videoPostRequestDTO.getVideoUrls() != null) {
            videoPost.getVideoUrls().clear();
            videoPost.getVideoUrls().addAll(videoPostRequestDTO.getVideoUrls());
        }

        return videoPost;
    }

    @Transactional
    protected LinkPost updateLinkPost(LinkPost linkPost, LinkPostRequestDTO linkPostRequestDTO) {
        if (linkPostRequestDTO.getTitle() != null) {
            linkPost.setTitle(linkPostRequestDTO.getTitle());
        }
        if (linkPostRequestDTO.getDescription() != null) {
            linkPost.setDescription(linkPostRequestDTO.getDescription());
        }
        if (linkPostRequestDTO.getLinks() != null) {
            linkPost.getLinks().clear();
            linkPost.getLinks().addAll(linkPostRequestDTO.getLinks());
        }

        return linkPost;
    }

    private Tag findOrCreateTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> tagService.saveCommonTag(name));
    }
}
