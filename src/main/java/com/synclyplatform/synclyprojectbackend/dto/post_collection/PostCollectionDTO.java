package com.synclyplatform.synclyprojectbackend.dto.post_collection;

import com.synclyplatform.synclyprojectbackend.dto.post.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCollectionDTO {

    private Long id;
    private Long userId;
    private String title;
    private String color;
    private boolean isDefault;
    private List<PostDTO> posts;
}
