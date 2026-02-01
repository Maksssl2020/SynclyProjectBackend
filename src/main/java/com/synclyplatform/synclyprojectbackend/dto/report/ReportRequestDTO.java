package com.synclyplatform.synclyprojectbackend.dto.report;

import com.synclyplatform.synclyprojectbackend.model.report.ReportReasonType;
import com.synclyplatform.synclyprojectbackend.model.report.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDTO {

    private Long entityId;
    private String reason;
    private String title;
    private ReportReasonType reportReasonType;
    private ReportType reportType;
}
