package com.synclyplatform.synclyprojectbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synclyplatform.synclyprojectbackend.dto.media.MediaRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.post.*;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ObjectMapper objectMapper;

    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> searchPostsByQuery(@RequestParam("query") String query) {
        return ResponseEntity.ok(postService.searchPostsByQuery(query));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getAllPostsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(postService.getPostsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/user/dashboard/{userId}")
    public ResponseEntity<List<PostDTO>> getRandomPostsForUserDashboardByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(postService.getRandomPostsForUserDashboard(userId), HttpStatus.OK);
    }


    @PostMapping(value = "/create/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> createPost(
            @PathVariable Long userId,
            @RequestPart(value = "data") String jsonData,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws JsonProcessingException {
        PostRequestDTO postRequestDTO = objectMapper.readValue(jsonData, PostRequestDTO.class);

        if (postRequestDTO instanceof PhotoPostRequestDTO photoDto && files != null) {
            List<MediaRequestDTO> mediaList = files.stream()
                    .map(file -> new MediaRequestDTO(null, file, com.synclyplatform.synclyprojectbackend.dto.media.MediaType.IMAGE))
                    .toList();
            photoDto.setPhotos(mediaList);
        }

        if (postRequestDTO instanceof AudioPostRequestDTO audioDto && files != null && files.size() == 1) {
            audioDto.setAudio(new MediaRequestDTO(null, files.get(0), com.synclyplatform.synclyprojectbackend.dto.media.MediaType.AUDIO));
        }

        if (postRequestDTO instanceof VideoPostRequestDTO videoDto && files != null) {
            List<MediaRequestDTO> videoList = files.stream()
                    .map(file -> new MediaRequestDTO(null, file, com.synclyplatform.synclyprojectbackend.dto.media.MediaType.VIDEO))
                    .toList();
            videoDto.setVideos(videoList);
        }

        postService.save(userId, postRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
