package com.synclyplatform.synclyprojectbackend.dto.comment;

import com.synclyplatform.synclyprojectbackend.model.image.Image;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostCommentDTO {

    private Long id;
    private String content;
    private String createdAt;
    private String updatedAt;

    private Long authorId;
    private Image authorImage;
    private String authorUsername;
    private String authorName;

    private Long postId;

    private Long parentId;

    private List<PostCommentDTO> replies;
    private List<Long> likesBy;
}
