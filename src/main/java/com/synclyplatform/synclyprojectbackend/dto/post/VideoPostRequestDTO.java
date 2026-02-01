package com.synclyplatform.synclyprojectbackend.dto.post;

import com.synclyplatform.synclyprojectbackend.dto.media.MediaRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.post.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VideoPostRequestDTO extends PostRequestDTO {

    @NotBlank(message = "Description cannot be empty.")
    private String description;

    @Size(min = 1, max = 4, message = "Post must contain between 1 and 4 video urls.")
    private List<@URL(message = "Please provide a valid URL.") String> videoUrls;
}
