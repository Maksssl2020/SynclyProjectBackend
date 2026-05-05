package com.synclyplatform.synclyprojectbackend.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminTagRequestDTO {

    @NotBlank(message = "Name cannot be empty.")
    private String name;

    @NotBlank(message = "Color cannot be blank.")
    private String color;

    @NotBlank(message = "Category name cannot be empty.")
    private String tagCategoryName;
}
