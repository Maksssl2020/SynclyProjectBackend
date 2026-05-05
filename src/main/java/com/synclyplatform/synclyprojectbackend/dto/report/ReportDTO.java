package com.synclyplatform.synclyprojectbackend.dto.report;

import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    private Long id;
    private String entityId;
    private UserDTO reportedBy;
    private UserDTO resolvedBy;
    private String title;
    private String reason;
    private String reportedAt;
    private String resolvedAt;
    private String reportStatus;
    private String reportReasonType;
    private ReportType reportType;
}
