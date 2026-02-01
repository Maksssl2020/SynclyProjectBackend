package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.search.SearchDTO;
import com.synclyplatform.synclyprojectbackend.dto.search.SearchRequestDTO;
import com.synclyplatform.synclyprojectbackend.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/trending")
    public ResponseEntity<List<SearchDTO>> trendingSearches() {
        return ResponseEntity.ok(searchService.getTrendingSearches());
    }

    @PostMapping("/save")
    public ResponseEntity<HttpStatus> saveSearch(@RequestBody SearchRequestDTO searchRequestDTO) {
        searchService.save(searchRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
