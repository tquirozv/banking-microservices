package com.bank.client_service.service;

import com.bank.client_service.dto.ClientDto;
import com.bank.client_service.dto.ClientUpdateDto;

import java.util.List;

public interface ClientService {
    ClientDto createClient(ClientDto clientDto);
    ClientDto getClientById(Long id);
    ClientDto getClientByIdentificacion(String identificacion);
    List<ClientDto> getAllClients();
    List<ClientDto> getClientsByEstado(Boolean estado);
    ClientDto updateClient(Long id, ClientUpdateDto clientDto);
    void deleteClient(Long id);
}