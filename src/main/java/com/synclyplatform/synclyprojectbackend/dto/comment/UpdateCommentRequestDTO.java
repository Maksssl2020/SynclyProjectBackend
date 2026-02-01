package com.synclyplatform.synclyprojectbackend.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequestDTO {

    private Long commentId;

    @NotBlank(message = "Updated content cannot be empty.")
    private String content;
}
