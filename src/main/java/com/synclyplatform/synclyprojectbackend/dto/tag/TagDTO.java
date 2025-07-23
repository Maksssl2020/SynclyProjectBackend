package com.synclyplatform.synclyprojectbackend.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {

    private Long id;
    private String name;
    private String description;
    private long postsCount;
    private long followersCount;
    private boolean trending;
    private String type;
    private String tagCategory;
    private String color;
    private String createdAt;
}
