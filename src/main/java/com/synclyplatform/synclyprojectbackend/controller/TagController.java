package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.tag.*;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.findAllTags());
    }

    @GetMapping("/tag/by-name")
    public ResponseEntity<TagDTO> getTagByName(@Param("tagName") String tagName) {
        return new ResponseEntity<>(tagService.getTagByName(tagName), HttpStatus.OK);
    }

    @GetMapping("/related/by-category")
    public ResponseEntity<List<TagDTO>> getRelatedTagsByCategory(@Param("category") String category) {
        return new ResponseEntity<>(tagService.findRelatedTagsByCategory(category), HttpStatus.OK);
    }

    @GetMapping("/android-app")
    public ResponseEntity<List<TagDTO>> getAllTagsAndroidApp() {
        return ResponseEntity.ok(tagService.findAllTags());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<TagUsageDTO>> getPopularTags(
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok(tagService.findPopularTags(limit));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<TagUsageDTO>> getTrendingTags(
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok(tagService.findTrendingTags(limit));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TagDTO>> searchTagsByQuery(@Param("query") String query) {
        return new ResponseEntity<>(tagService.searchTags(query), HttpStatus.OK);
    }

    @GetMapping("/exists/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        return ResponseEntity.ok(tagService.tagExists(name));
    }

    @PostMapping("/create/main")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpStatus> createMain(@AuthenticationPrincipal User adminUser, @RequestBody AdminTagRequestDTO mainTagRequest) {
        tagService.saveMainTag(adminUser, mainTagRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/create/common")
    public ResponseEntity<TagDTO> createCommon(@RequestBody CommonTagRequestDTO commonTagRequest) {
        return new ResponseEntity<>(tagService.saveCommonTag(commonTagRequest), HttpStatus.CREATED);
    }

    @PatchMapping("/change/category")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<HttpStatus> changeTagCategory(@AuthenticationPrincipal User adminUser, @RequestBody ChangeTagCategoryRequestDTO changeTagCategoryRequestDTO) {
        tagService.changeTagCategory(adminUser, changeTagCategoryRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
