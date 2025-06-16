package com.synclyplatform.synclyprojectbackend.model.post;

import com.synclyplatform.synclyprojectbackend.model.audio.Audio;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AudioPost extends Post{

    @NotBlank(message = "Song title cannot be empty.")
    private String songTitle;

    @NotBlank(message = "Artist name cannot be empty.")
    private String artist;

    @NotBlank(message = "Please provide your thoughts.")
    private String yourThoughts;

    @OneToOne
    private Audio audio;
}
