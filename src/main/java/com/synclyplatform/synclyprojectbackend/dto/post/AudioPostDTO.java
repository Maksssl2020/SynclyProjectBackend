package com.synclyplatform.synclyprojectbackend.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AudioPostDTO extends PostDTO {

    private String songTitle;
    private String artist;
    private String yourThoughts;
    private String audioUrl;
}
