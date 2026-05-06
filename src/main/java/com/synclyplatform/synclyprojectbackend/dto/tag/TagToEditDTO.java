package com.synclyplatform.synclyprojectbackend.dto.tag;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagToEditDTO {

    private Long id;
    private Long tagCategoryId;
    private String name;
    private String color;
    private String tagCategoryName;
}
