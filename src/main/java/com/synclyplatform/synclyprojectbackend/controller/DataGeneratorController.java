package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.service.data_generator.DataGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/v1/generate-data")
@RequiredArgsConstructor
public class DataGeneratorController {

    private final DataGeneratorService dataGeneratorService;

    @PostMapping("/users/{count}")
    public ResponseEntity<HttpStatus> generateUsers(@PathVariable int count) throws Exception {
        for (int i = 0; i < count; i++) {
            dataGeneratorService.generateUser();
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/posts/{count}")
    public ResponseEntity<HttpStatus> generateUserPosts(@PathVariable int count) {
        for (int i = 0; i < count; i++) {
            dataGeneratorService.generateUserPosts();
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/avatar")
    public ResponseEntity<String> generateAvatar(@RequestBody String seed) {
        return new ResponseEntity<>(dataGeneratorService.generateAvatarUrl(seed), HttpStatus.CREATED);
    }
}
