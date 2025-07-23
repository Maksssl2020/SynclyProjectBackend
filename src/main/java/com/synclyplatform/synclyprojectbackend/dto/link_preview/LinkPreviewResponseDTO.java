package com.synclyplatform.synclyprojectbackend.dto.link_preview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkPreviewResponseDTO {

    private String title;
    private String description;
    private String link;
    private String image;
}
