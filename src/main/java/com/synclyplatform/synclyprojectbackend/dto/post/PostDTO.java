package com.synclyplatform.synclyprojectbackend.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PostDTO {

    private Long id;
    private String createdAt;
    private String updatedAt;
    private String postType;
    private Long authorId;
    private String authorName;
    private String authorUsername;
    private List<String> tags;
    private List<Long> likesBy;
    private List<Long> savedBy;
    private long commentsCount;
}
