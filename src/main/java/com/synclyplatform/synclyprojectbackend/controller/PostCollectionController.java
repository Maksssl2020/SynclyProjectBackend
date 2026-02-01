package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.post_collection.PostCollectionDTO;
import com.synclyplatform.synclyprojectbackend.dto.post_collection.PostCollectionRequestDTO;
import com.synclyplatform.synclyprojectbackend.service.post_collection.PostCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post-collections")
@RequiredArgsConstructor
public class PostCollectionController {

    private final PostCollectionService postCollectionService;

    @GetMapping("/{postCollectionId}")
    public ResponseEntity<PostCollectionDTO> getPostCollectionById(@PathVariable Long postCollectionId) {
        return new ResponseEntity<>(postCollectionService.getPostCollectionById(postCollectionId), HttpStatus.OK);
    }

    @GetMapping("/android-app/{postCollectionId}")
    public ResponseEntity<PostCollectionDTO> getPostCollectionByIdAndroid(@PathVariable Long postCollectionId) {
        return new ResponseEntity<>(postCollectionService.getPostCollectionById(postCollectionId), HttpStatus.OK);
    }


    @GetMapping("/all-by-user/{userId}")
    public ResponseEntity<List<PostCollectionDTO>> getAllPostCollectionsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(postCollectionService.getPostCollectionByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/exists/{userId}")
    public ResponseEntity<Boolean> getPostCollectionsByUserId(@PathVariable Long userId, @RequestParam("title")  String title) {
        return new ResponseEntity<>(postCollectionService.existByNameAndUserId(userId, title), HttpStatus.OK);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<HttpStatus> createPostCollection(@PathVariable Long userId, @RequestBody PostCollectionRequestDTO postCollectionRequestDTO) {
        postCollectionService.savePostCollection(userId, postCollectionRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/save-post/{postCollectionId}")
    public ResponseEntity<HttpStatus> savePostInCollection(@PathVariable Long postCollectionId, @RequestParam("postId") Long postId) {
        postCollectionService.savePostInCollection(postId, postCollectionId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/unsave-post/{userId}/{postId}")
    public ResponseEntity<HttpStatus> unsavePostFromCollection(@PathVariable Long userId, @PathVariable Long postId) {
        postCollectionService.unsavePostFromCollection(postId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/unsave-post/by-post-collection/{postCollectionId}/{postId}")
    public ResponseEntity<HttpStatus> unsavePostFromCollectionByPostCollectionId(@PathVariable Long postCollectionId, @PathVariable Long postId) {
        postCollectionService.unsavePostFromCollectionByPostCollectionId(postId, postCollectionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
