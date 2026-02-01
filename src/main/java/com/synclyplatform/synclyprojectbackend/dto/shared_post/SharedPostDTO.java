package com.synclyplatform.synclyprojectbackend.dto.shared_post;


import com.synclyplatform.synclyprojectbackend.dto.post.PostDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SharedPostDTO {

    private Long id;
    private UserDTO sharedBy;
    private PostDTO originalPost;
    private String sharedAt;
}
