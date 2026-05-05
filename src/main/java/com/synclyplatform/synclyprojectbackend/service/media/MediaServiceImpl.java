package com.synclyplatform.synclyprojectbackend.service.media;

import com.synclyplatform.synclyprojectbackend.dto.media.MediaDTO;
import com.synclyplatform.synclyprojectbackend.dto.media.MediaRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.media.MediaType;
import com.synclyplatform.synclyprojectbackend.model.image.Image;
import com.synclyplatform.synclyprojectbackend.model.post.PhotoPost;
import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.post.PostType;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.model.video.Video;
import com.synclyplatform.synclyprojectbackend.repository.ImageRepository;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserProfileRepository;
import com.synclyplatform.synclyprojectbackend.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;
    private final UserProfileRepository userProfileRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void saveUserAvatar(Long userId, MultipartFile image) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found."));

        try {
            byte[] imageBytes = image.getBytes();
            Image imageEntity = Image.builder()
                    .imageData(imageBytes)
                    .build();


            Image profileImage = userProfile.getProfileImage();

            if (profileImage != null) {
                imageRepository.delete(profileImage);
            }

            imageRepository.save(imageEntity);
            userProfile.setProfileImage(imageEntity);
            userProfileRepository.save(userProfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void savePostPhotos(PhotoPost photoPost, List<MediaRequestDTO> mediaRequestDTOList) {
        List<Image> postPhotos = new ArrayList<>();

        mediaRequestDTOList.forEach(mediaRequestDTO -> {
            if (mediaRequestDTO.getMediaType().equals(MediaType.IMAGE)) {
                Image imageEntity = createImage(mediaRequestDTO);

                imageRepository.save(imageEntity);
                postPhotos.add(imageEntity);
            }
        });

        if (!postPhotos.isEmpty()) {
            photoPost.setImages(postPhotos);
        }
    }

    @Override
    public MultipartFile base64ToMultipartFile(String base64, String fileName) {
        byte[] decoded = Base64.getDecoder().decode(base64);

        try {
            return new MockMultipartFile(
                    fileName,
                    fileName,
                    "image/png",
                    IOUtils.toInputStream(new String(decoded), "ISO_8859_1")
            );
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<MediaDTO> getPostPhotos(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found."));

        if (!post.getPostType().equals(PostType.PHOTO)) {
            throw new IllegalStateException("Post type is not PHOTO.");
        }

        if (post instanceof PhotoPost photoPost) {
            return photoPost.getImages().stream().map(imageEntity -> {
                if (imageEntity.getUrl() != null) {
                    return MediaDTO.builder()
                            .entityId(imageEntity.getImageId())
                            .url(imageEntity.getUrl())
                            .build();
                }

                return MediaDTO.builder()
                        .entityId(imageEntity.getImageId())
                        .mediaFile(imageEntity.getImageData())
                        .build();
            })
                    .toList();
        }

        return List.of();
    }

    private Image createImage(MediaRequestDTO dto) {
        Image image = new Image();

        if (dto.getMediaFile() != null) {
            try {
                byte[] bytes = dto.getMediaFile().getBytes();
                image.setImageData(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            image.setUrl(dto.getUrl());
        }

        return image;
    }

    private Video createVideo(MediaRequestDTO dto) {
        Video video = new Video();

        if (dto.getMediaFile() != null) {
            try {
                byte[] bytes = dto.getMediaFile().getBytes();
                video.setVideoData(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            video.setUrl(dto.getUrl());
        }

        return video;
    }

}
