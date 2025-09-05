package com.bank.account_service.service;

import com.bank.account_service.dto.AccountWithMovementsDto;
import com.bank.account_service.dto.ReportDto;
import com.bank.account_service.utils.ToDto;
import com.bank.account_service.dto.ClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService{

    private final WebClient clientServiceWebClient;
    private final AccountService accountService;

    @Autowired
    public ReportServiceImpl(WebClient clientServiceWebClient, AccountService accountService) {
        this.clientServiceWebClient = clientServiceWebClient;
        this.accountService = accountService;
    }

    @Override
    public List<ReportDto> getReportByPersonaId(String personaId, LocalDateTime startDate, LocalDateTime endDate) {
        ClientDto client = getClientByIdentificacion(personaId);
        List<AccountWithMovementsDto> accounts =
                accountService.getAccountsByClientIdAndDateRange(client.getClienteId(), startDate, endDate);
        return ToDto.reportCreation(client, accounts);
    }

    @Override
    public List<ReportDto> getReportByClientId(Long clientId, LocalDateTime startDate, LocalDateTime endDate) {
        ClientDto client = getClientByClientId(clientId);
        List<AccountWithMovementsDto> accounts =
                accountService.getAccountsByClientIdAndDateRange(client.getClienteId(), startDate, endDate);
        return ToDto.reportCreation(client, accounts);
    }

    private ClientDto getClientByIdentificacion(String personaId) {
        return clientServiceWebClient.get()
                .uri("/api/clientes/identificacion/" + personaId)
                .retrieve()
                .bodyToMono(ClientDto.class)
                .block();
    }

    private ClientDto getClientByClientId(Long clientId) {
        return  clientServiceWebClient.get()
                .uri("/api/clientes/" + clientId)
                .retrieve()
                .bodyToMono(ClientDto.class)
                .block();
    }
}
