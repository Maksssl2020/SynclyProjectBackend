package com.synclyplatform.synclyprojectbackend.dto.post;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequestDTO {
    private Long postId;
    private PostRequestDTO updatedData;
}
