package com.synclyplatform.synclyprojectbackend.dto.tag;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagUpdateRequestDTO {

    private Long tagId;
    private Long tagCategoryId;
    private String tagName;
    private String color;}
