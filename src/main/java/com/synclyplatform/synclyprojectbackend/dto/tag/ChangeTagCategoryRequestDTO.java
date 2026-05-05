package com.synclyplatform.synclyprojectbackend.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeTagCategoryRequestDTO {

    private Long tagId;
    private Long categoryId;
}
