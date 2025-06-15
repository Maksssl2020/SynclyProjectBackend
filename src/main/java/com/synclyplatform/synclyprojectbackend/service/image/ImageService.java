package com.synclyplatform.synclyprojectbackend.service.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {
    void saveUserAvatar(Long userId, MultipartFile image);
}
