package com.synclyplatform.synclyprojectbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synclyplatform.synclyprojectbackend.dto.media.MediaRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.post.*;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.utils.TimestampSortOption;
import com.synclyplatform.synclyprojectbackend.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/android-app/user/{userId}")
    public ResponseEntity<List<PostDTO>> getAllPostsByUserIdForAndroidApp(@PathVariable Long userId) {
        return new ResponseEntity<>(postService.getPostsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/android-app/user/dashboard/{userId}")
    public ResponseEntity<List<PostDTO>> getUserForYouFeedForAndroid(
            @PathVariable Long userId,
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit
    ) {
        return new ResponseEntity<>(postService.getForYouFeed(userId, offset, limit), HttpStatus.OK);
    }

    @GetMapping("/android-app/user/{userId}/feed/following")
    public ResponseEntity<List<PostDTO>> getUserFollowedFeedForAndroid(
            @PathVariable Long userId,
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit
    ) {
        return new ResponseEntity<>(postService.getFollowedFeed(userId, offset, limit), HttpStatus.OK);
    }


    @GetMapping("/user/dashboard/{userId}")
    public ResponseEntity<List<PostDTO>> getUserForYouFeed(
            @PathVariable Long userId,
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit
    ) {
        return new ResponseEntity<>(postService.getForYouFeed(userId, offset, limit), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/feed/following")
    public ResponseEntity<List<PostDTO>> getUserFollowedFeed(
            @PathVariable Long userId,
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit
    ) {
        return new ResponseEntity<>(postService.getFollowedFeed(userId, offset, limit), HttpStatus.OK);
    }

    @GetMapping("/by-tag")
    public ResponseEntity<List<PostDTO>> getAllPostsByTag(
            @RequestParam("tag") String tag,
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("sortOption") TimestampSortOption sortOption
    ) {
        return new ResponseEntity<>(postService.getPostsByTag(tag, offset, limit, sortOption), HttpStatus.OK);
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

        postService.save(userId, postRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/android-app/create/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> createPostAndroid(
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

        postService.save(userId, postRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/update")
    public ResponseEntity<HttpStatus> updatePost(
            @RequestPart(value = "data") UpdatePostRequestDTO updatePostRequestDTO,
            @RequestPart(value = "photoFiles", required = false) List<MultipartFile> photoFiles

    ) {
        if (updatePostRequestDTO.getUpdatedData() instanceof PhotoPostRequestDTO photoPostRequestDTO && photoFiles != null) {
            List<MediaRequestDTO> mediaList = photoFiles.stream()
                    .map(file -> new MediaRequestDTO(null, file, com.synclyplatform.synclyprojectbackend.dto.media.MediaType.IMAGE))
                    .toList();
            photoPostRequestDTO.setPhotos(mediaList);
        }

        postService.update(updatePostRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deletePost(@AuthenticationPrincipal User user, @RequestParam("postId") Long postId) {
        postService.deletePost(user, postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
