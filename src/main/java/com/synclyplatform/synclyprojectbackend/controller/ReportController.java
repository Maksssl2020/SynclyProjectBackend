package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.report.AndroidReportRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.report.ReportDTO;
import com.synclyplatform.synclyprojectbackend.dto.report.ReportRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.report.ResolveReportRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<ReportDTO>> getAllReports(){
        return ResponseEntity.ok(reportService.findAll());
    }

    @PostMapping("/report")
    public ResponseEntity<HttpStatus> createReport(@RequestBody ReportRequestDTO reportRequestDTO, @AuthenticationPrincipal User user) {
        reportService.report(user, reportRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/android-app/report")
    public ResponseEntity<HttpStatus> createReportAndroidApp(@RequestBody AndroidReportRequestDTO androidReportRequestDTO) {
        reportService.report(androidReportRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/resolve")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<HttpStatus> resolveReport(
            @AuthenticationPrincipal User user,
            @RequestBody ResolveReportRequestDTO resolveReportRequestDTO
    ) {
        reportService.resolve(user, resolveReportRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
