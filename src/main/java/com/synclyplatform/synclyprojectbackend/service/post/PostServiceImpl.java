package com.synclyplatform.synclyprojectbackend.service.post;

import com.synclyplatform.synclyprojectbackend.dto.post.*;
import com.synclyplatform.synclyprojectbackend.model.post.*;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.media.MediaService;
import com.synclyplatform.synclyprojectbackend.utils.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
            case AudioPostRequestDTO audioPostRequestDTO -> {
                AudioPost mappedAudioPost = postMapper.fromRequestDto(audioPostRequestDTO);

                mappedAudioPost.setAuthor(foundUser);
                mediaService.savePostAudio(mappedAudioPost, audioPostRequestDTO.getAudio());
                mappedAudioPost.setPostType(PostType.MUSIC);
                postRepository.save(mappedAudioPost);
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
    public List<PostDTO> getRandomPostsForUserDashboard(Long userId) {
        return postRepository.findRandomPostsExcludingUser(userId, 100).stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

}
