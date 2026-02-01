package com.synclyplatform.synclyprojectbackend.model.post;

import com.synclyplatform.synclyprojectbackend.model.video.Video;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VideoPost extends Post {


    @NotBlank(message = "Description cannot be empty.")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @Size(min = 1, message = "At least one video link must be provided.")
    private List<@URL(message = "Please provide a valid URL.") String> videoUrls;
}
