package com.synclyplatform.synclyprojectbackend.dto.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {

    private Long entityId;
    private String url;
    private byte[] mediaFile;
    private MediaType mediaType;
}
