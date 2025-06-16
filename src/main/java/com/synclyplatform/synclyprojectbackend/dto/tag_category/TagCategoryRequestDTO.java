package com.synclyplatform.synclyprojectbackend.dto.tag_category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagCategoryRequestDTO {

    @NotBlank(message = "Name cannot be empty.")
    private String name;

    @NotBlank(message = "Description cannot be empty.")
    private String description;

    @NotBlank(message = "Color cannot be empty.")
    private String color;
}
