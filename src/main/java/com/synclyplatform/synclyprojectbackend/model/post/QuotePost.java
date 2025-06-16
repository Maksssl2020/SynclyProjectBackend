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
public class QuotePost extends Post {

    @NotBlank(message = "Quote cannot be empty.")
    private String quote;

    private String source;
}
