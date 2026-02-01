package com.synclyplatform.synclyprojectbackend.dto.tag;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostTagDTO {
    private Long id;
    private String name;
    private String color;
}
