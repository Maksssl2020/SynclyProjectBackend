package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.media.MediaDTO;
import com.synclyplatform.synclyprojectbackend.service.media.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @GetMapping("/post/photos")
    public ResponseEntity<List<MediaDTO>> getPostPhotos
    (@RequestParam("postId") Long postId) {
        return new ResponseEntity<>(mediaService.getPostPhotos(postId), HttpStatus.OK);
    }
}
