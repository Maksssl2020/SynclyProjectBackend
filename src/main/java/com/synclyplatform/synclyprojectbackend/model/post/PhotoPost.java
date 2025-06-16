package com.synclyplatform.synclyprojectbackend.model.post;

import com.synclyplatform.synclyprojectbackend.model.image.Image;
import jakarta.persistence.*;
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
public class PhotoPost extends Post {

    @NotBlank(message = "Caption cannot be empty.")
    private String caption;

    @Size(min = 1, max = 8, message = "Post must contain between 1 and 8 photos.")
    @OneToMany
    @JoinTable(
            name = "photo_post_images",
            joinColumns = @JoinColumn(name = "photo_post_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    List<Image> images;
}
