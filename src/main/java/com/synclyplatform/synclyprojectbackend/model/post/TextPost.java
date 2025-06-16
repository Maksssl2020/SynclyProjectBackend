package com.synclyplatform.synclyprojectbackend.model.post;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TextPost extends Post {

    private String title;

    @NotBlank(message = "Content cannot be empty.")
    private String content;
}
