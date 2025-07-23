package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.service.data_generator.DataGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/v1/generate-data")
@RequiredArgsConstructor
public class DataGeneratorController {

    private final DataGeneratorService dataGeneratorService;

    @PostMapping("/users/{count}")
    public ResponseEntity<HttpStatus> generateUsers(@PathVariable int count) throws MalformedURLException {
        for (int i = 0; i < count; i++) {
            dataGeneratorService.generateUser();
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
