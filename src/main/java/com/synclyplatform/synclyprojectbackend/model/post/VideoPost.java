package com.synclyplatform.synclyprojectbackend.model.post;

import com.synclyplatform.synclyprojectbackend.model.video.Video;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VideoPost extends Post {


    @NotBlank(message = "Description cannot be empty.")
    private String description;

    @OneToMany
    @JoinTable(
            name = "video_post_videos",
            joinColumns = @JoinColumn(name = "video_post_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id")
    )
    @Size(min = 1, max = 4)
    private List<Video> videos;
}
