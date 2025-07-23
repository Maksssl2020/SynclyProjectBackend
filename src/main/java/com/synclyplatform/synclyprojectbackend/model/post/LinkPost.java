package com.synclyplatform.synclyprojectbackend.model.post;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class LinkPost extends Post {

    private String title;

    @NotBlank(message = "Description cannot be empty.")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @Size(min = 1, message = "At least one link must be provided.")
    List<@URL(message = "Please provide a valid URL.") String> links;
}
