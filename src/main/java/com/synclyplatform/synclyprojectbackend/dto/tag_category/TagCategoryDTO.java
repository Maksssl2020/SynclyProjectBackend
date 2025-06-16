package com.synclyplatform.synclyprojectbackend.dto.tag_category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagCategoryDTO {

    private Long id;
    private String name;
    private String description;
    private String color;
    private long tagCount;
}
