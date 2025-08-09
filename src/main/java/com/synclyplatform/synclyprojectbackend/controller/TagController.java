package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.tag.CommonTagRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.MainTagRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag.TagUsageDTO;
import com.synclyplatform.synclyprojectbackend.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<HttpStatus> createMain(@RequestBody MainTagRequestDTO mainTagRequest) {
        tagService.saveMainTag(mainTagRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/create/common")
    public ResponseEntity<HttpStatus> createCommon(@RequestBody CommonTagRequestDTO commonTagRequest) {
        tagService.saveCommonTag(commonTagRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
