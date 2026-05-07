package com.synclyplatform.synclyprojectbackend.dto.tag;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagStatsAdminDTO {

    private Long totalTags;
    private Long totalTrendingTags;
    private Long totalPosts;
    private Long totalFollowers;
}
