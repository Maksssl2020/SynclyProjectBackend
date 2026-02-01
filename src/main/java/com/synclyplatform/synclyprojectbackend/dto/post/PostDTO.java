package com.synclyplatform.synclyprojectbackend.dto.post;

import com.synclyplatform.synclyprojectbackend.dto.tag.PostTagDTO;
import com.synclyplatform.synclyprojectbackend.model.image.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Image authorAvatar;
    private List<PostTagDTO> tags = new ArrayList<>();
    private List<Long> likesBy = new ArrayList<>();
    private List<Long> savedBy = new ArrayList<>();
    private List<Long> sharedBy = new ArrayList<>();
    private long commentsCount;
    private long likesCount;
}
