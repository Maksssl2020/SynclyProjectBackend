package com.synclyplatform.synclyprojectbackend.dto.post;


import com.synclyplatform.synclyprojectbackend.dto.media.MediaRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PhotoPostRequestDTO extends PostRequestDTO {

    @NotBlank(message = "Caption cannot be empty.")
    private String caption;

    @Size(min = 1, max = 8, message = "Post must contain between 1 and 8 photos.")
    private List<MediaRequestDTO> photos;
}
