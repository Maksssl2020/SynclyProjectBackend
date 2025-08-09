package com.synclyplatform.synclyprojectbackend.dto.tag;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagUsageDTO {

    private Long id;
    private String name;
    private Long usageCount;
}
