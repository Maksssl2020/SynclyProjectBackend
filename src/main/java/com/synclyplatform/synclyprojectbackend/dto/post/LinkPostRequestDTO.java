package com.synclyplatform.synclyprojectbackend.dto.post;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LinkPostRequestDTO extends PostRequestDTO {

    private String title;

    @NotBlank(message = "Description cannot be empty.")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @Size(min = 1, message = "At least one link must be provided.")
    List<@URL(message = "Please provide a valid URL.") String> links;
}
