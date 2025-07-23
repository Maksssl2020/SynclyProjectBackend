package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.link_preview.LinkPreviewResponseDTO;
import com.synclyplatform.synclyprojectbackend.service.link_preview.LinkPreviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/site-preview")
@RequiredArgsConstructor
public class LinkPreviewController {

    private final LinkPreviewService linkPreviewService;

    @PostMapping
    public ResponseEntity<LinkPreviewResponseDTO> getPreview(@RequestPart("url") String url) throws IOException {
        System.out.println(url);
        LinkPreviewResponseDTO linkPreviewResponseDTO = linkPreviewService.fetchMetadata(url);
        return ResponseEntity.ok(linkPreviewResponseDTO);
    }
}
