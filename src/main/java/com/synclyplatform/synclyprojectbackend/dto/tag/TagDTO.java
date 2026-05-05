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
    private long postsCount;
    private long followersCount;
    private int postsThisWeek;
    private boolean trending;
    private String type;
    private String tagCategory;
    private String color;
    private String createdAt;
}
