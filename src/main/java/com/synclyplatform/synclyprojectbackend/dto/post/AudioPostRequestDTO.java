package com.synclyplatform.synclyprojectbackend.dto.post;

import com.synclyplatform.synclyprojectbackend.dto.media.MediaRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AudioPostRequestDTO extends PostRequestDTO{

    @NotBlank(message = "Song title cannot be empty.")
    private String songTitle;

    @NotBlank(message = "Artist name cannot be empty.")
    private String artist;

    @NotBlank(message = "Please provide your thoughts.")
    private String yourThoughts;

    @NotNull(message = "Audio cannot be empty.")
    private MediaRequestDTO  audio;
}
