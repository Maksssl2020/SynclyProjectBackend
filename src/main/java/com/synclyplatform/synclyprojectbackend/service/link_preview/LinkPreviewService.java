package com.synclyplatform.synclyprojectbackend.service.link_preview;

import com.synclyplatform.synclyprojectbackend.dto.link_preview.LinkPreviewResponseDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface LinkPreviewService {
    LinkPreviewResponseDTO fetchMetadata(String url) throws IOException;
}
