package com.synclyplatform.synclyprojectbackend.dto.report;

import com.synclyplatform.synclyprojectbackend.model.report.ReportReasonType;
import com.synclyplatform.synclyprojectbackend.model.report.ReportType;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDTO {

    private Long entityId;
    private String reason;
    private String title;
    private ReportReasonType reportReasonType;
    private ReportType reportType;
}
