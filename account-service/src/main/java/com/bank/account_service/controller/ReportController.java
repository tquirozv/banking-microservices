package com.bank.account_service.controller;

import com.bank.account_service.dto.ReportDto;
import com.bank.account_service.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/reportes")
public class ReportController {

    private final ReportService reportService;

    public ReportController(final ReportService reportService) {
        // Using final parameter and direct assignment for immutability
        this.reportService = Objects.requireNonNull(reportService, "AccountService must not be null");
    }

    @GetMapping("/client/{clientId}/date-range")
    public ResponseEntity<List<ReportDto>> getReportByClientId(
            @PathVariable Long clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(reportService.getReportByClientId(clientId, startDate, endDate));
    }

    @GetMapping("/persona/{personaId}/date-range")
    public ResponseEntity<List<ReportDto>> getReportByPersonaId(
            @PathVariable String personaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(reportService.getReportByPersonaId(personaId, startDate, endDate));
    }
}
