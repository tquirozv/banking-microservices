package com.bank.client_service.controller;

import com.bank.client_service.dto.ClientDto;
import com.bank.client_service.dto.ClientUpdateDto;
import com.bank.client_service.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody ClientDto clientDto) {
        ClientDto createdClient = clientService.createClient(clientDto);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        ClientDto client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<ClientDto> getClientByIdentificacion(@PathVariable String identificacion) {
        ClientDto client = clientService.getClientByIdentificacion(identificacion);
        return ResponseEntity.ok(client);
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients(@RequestParam(required = false) Boolean estado) {
        List<ClientDto> clients = estado != null ? 
            clientService.getClientsByEstado(estado) : 
            clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, 
                                                  @Valid @RequestBody ClientUpdateDto clientUpdateDto) {
        ClientDto updatedClient = clientService.updateClient(id, clientUpdateDto);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}