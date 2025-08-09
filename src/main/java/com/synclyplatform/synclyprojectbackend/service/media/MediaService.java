package com.synclyplatform.synclyprojectbackend.service.media;

import com.synclyplatform.synclyprojectbackend.dto.media.MediaRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.post.PhotoPost;
import com.synclyplatform.synclyprojectbackend.model.post.VideoPost;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface MediaService {

    void saveUserAvatar(Long userId, MultipartFile image);
    void savePostPhotos(PhotoPost photoPost, List<MediaRequestDTO> mediaRequestDTOList);
    void savePostVideos(VideoPost videoPost, List<MediaRequestDTO> mediaRequestDTOList);
    MultipartFile base64ToMultipartFile(String base64, String fileName);
}
