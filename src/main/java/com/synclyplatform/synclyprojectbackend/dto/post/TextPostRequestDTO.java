package com.synclyplatform.synclyprojectbackend.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TextPostRequestDTO extends PostRequestDTO {

    private String title;

    @NotBlank(message = "Content cannot be empty.")
    private String content;
}
