package com.synclyplatform.synclyprojectbackend.model.tag;

import com.synclyplatform.synclyprojectbackend.model.tag_category.TagCategory;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    private String color;
    private String description;
    private boolean trending;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private TagType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_category_id")
    private TagCategory tagCategory;
}
