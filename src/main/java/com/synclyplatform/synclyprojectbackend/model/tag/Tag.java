package com.synclyplatform.synclyprojectbackend.model.tag;

import com.synclyplatform.synclyprojectbackend.model.tag_category.TagCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,  nullable = false)
    @NotBlank(message = "Name cannot be empty.")
    private String name;

    private String description;
    private boolean trending;

    @Enumerated(EnumType.STRING)
    private TagType type;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private TagCategory tagCategory;
}
