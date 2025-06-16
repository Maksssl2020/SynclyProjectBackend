package com.synclyplatform.synclyprojectbackend.model.tag_category;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Name cannot be empty.")
    private String name;

    @NotBlank(message = "Description cannot be empty.")
    private String description;

    @NotBlank(message = "Color cannot be empty.")
    private String color;
}
