package com.bank.account_service.service;

import com.bank.account_service.dto.ReportDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    List<ReportDto> getReportByClientId(Long clientId, LocalDateTime startDate, LocalDateTime endDate);

    List<ReportDto> getReportByPersonaId(String personaId, LocalDateTime startDate, LocalDateTime endDate);
}
