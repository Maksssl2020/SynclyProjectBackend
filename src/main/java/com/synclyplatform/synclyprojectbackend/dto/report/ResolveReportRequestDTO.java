package com.synclyplatform.synclyprojectbackend.dto.report;

import com.synclyplatform.synclyprojectbackend.model.report.ReportStatus;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResolveReportRequestDTO {

    private Long reportId;
    private ReportStatus reportStatus;
}
