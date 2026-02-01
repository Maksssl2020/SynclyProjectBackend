package com.synclyplatform.synclyprojectbackend.dto.admin;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPanelStatisticsDTO {

    private Long totalUsersCount;
    private Long usersChangeFromYesterday;

    private Long totalActiveReportsCount;
    private Long reportsChangeFromYesterday;

    private Long totalMainTagsCount;
    private Long tagsChangeFromYesterday;

    private Long postsToday;
    private Long postsChangeFromYesterday;
}
