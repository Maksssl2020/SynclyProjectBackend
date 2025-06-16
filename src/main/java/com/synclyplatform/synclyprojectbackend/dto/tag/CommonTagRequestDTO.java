package com.synclyplatform.synclyprojectbackend.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonTagRequestDTO {

    @NotBlank(message = "Name cannot be empty.")
    private String name;
}
