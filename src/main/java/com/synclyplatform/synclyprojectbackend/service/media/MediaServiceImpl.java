package com.synclyplatform.synclyprojectbackend.service.media;

import com.synclyplatform.synclyprojectbackend.dto.media.MediaRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.media.MediaType;
import com.synclyplatform.synclyprojectbackend.model.audio.Audio;
import com.synclyplatform.synclyprojectbackend.model.image.Image;
import com.synclyplatform.synclyprojectbackend.model.post.AudioPost;
import com.synclyplatform.synclyprojectbackend.model.post.PhotoPost;
import com.synclyplatform.synclyprojectbackend.model.post.VideoPost;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.model.video.Video;
import com.synclyplatform.synclyprojectbackend.repository.AudioRepository;
import com.synclyplatform.synclyprojectbackend.repository.ImageRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserProfileRepository;
import com.synclyplatform.synclyprojectbackend.repository.VideoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
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
    private final AudioRepository audioRepository;
    private final UserProfileRepository userProfileRepository;

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
    public void savePostVideos(VideoPost videoPost, List<MediaRequestDTO> mediaRequestDTOList) {
        List<Video> videoEntities = new ArrayList<>();

        mediaRequestDTOList.forEach(mediaRequestDTO -> {
            if (mediaRequestDTO.getMediaType().equals(MediaType.VIDEO)) {
                Video videoEntity = createVideo(mediaRequestDTO);

                videoRepository.save(videoEntity);
                videoEntities.add(videoEntity);
            }
        });

        if (!videoEntities.isEmpty()) {
            videoPost.setVideos(videoEntities);
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
    public void savePostAudio(AudioPost audioPost, MediaRequestDTO mediaRequestDTO) {
        if (mediaRequestDTO.getMediaType().equals(MediaType.AUDIO)) {
            Audio audioEntity = createAudio(mediaRequestDTO);

            audioRepository.save(audioEntity);
            audioPost.setAudio(audioEntity);
        }
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

    private Audio createAudio(MediaRequestDTO dto) {
        Audio audio = new Audio();

        if (dto.getMediaFile() != null) {
            try {
                byte[] bytes = dto.getMediaFile().getBytes();
                audio.setAudioData(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            audio.setUrl(dto.getUrl());
        }

        return audio;
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
