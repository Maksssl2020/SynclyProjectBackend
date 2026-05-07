package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.tag_category.TagCategoryDTO;
import com.synclyplatform.synclyprojectbackend.dto.tag_category.TagCategoryRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.service.tag_category.TagCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag-categories")
@RequiredArgsConstructor
public class TagCategoryController {

    private final TagCategoryService tagCategoryService;

    @GetMapping
    public ResponseEntity<List<TagCategoryDTO>> findAll() {
        return ResponseEntity.ok(tagCategoryService.findAll());
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllTagCategoriesNames() {
        return ResponseEntity.ok(tagCategoryService.getTagsCategoriesNames());
    }

    @GetMapping("/{tagCategoryId}")
    public ResponseEntity<TagCategoryDTO> findByTagCategoryId(@PathVariable Long tagCategoryId) {
        return ResponseEntity.ok(tagCategoryService.findById(tagCategoryId));
    }

    @GetMapping("/exists/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        return ResponseEntity.ok(tagCategoryService.tagCategoryExists(name));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpStatus> create(@AuthenticationPrincipal User adminUser, @RequestBody TagCategoryRequestDTO tagCategoryRequest) {
        tagCategoryService.save(adminUser, tagCategoryRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
