package com.synclyplatform.synclyprojectbackend.service.report;

import com.synclyplatform.synclyprojectbackend.dto.report.AndroidReportRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.report.ReportDTO;
import com.synclyplatform.synclyprojectbackend.dto.report.ReportRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.report.ResolveReportRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportService {

    List<ReportDTO> findAll();
    void report(User user, ReportRequestDTO reportRequestDTO);
    void resolve(User user, ResolveReportRequestDTO resolveReportRequestDTO);
    void report(AndroidReportRequestDTO androidReportRequestDTO);
}
