package com.synclyplatform.synclyprojectbackend.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentRequestDTO {
    private Long postId;
    private Long userId;

    @NotBlank(message = "Content cannot be empty.")
    private String content;
}
