package com.synclyplatform.synclyprojectbackend.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
