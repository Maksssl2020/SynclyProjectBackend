package com.synclyplatform.synclyprojectbackend.dto.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaRequestDTO {

    private String url;

    @JsonIgnore
    private MultipartFile mediaFile;
    private MediaType mediaType;
}
