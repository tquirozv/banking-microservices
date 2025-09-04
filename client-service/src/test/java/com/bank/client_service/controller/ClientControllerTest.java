package com.bank.client_service.controller;

import com.bank.client_service.dto.ClientDto;
import com.bank.client_service.dto.ClientUpdateDto;
import com.bank.client_service.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController controller;

    @Test
    void createClient_shouldReturnCreated() {
        ClientDto input = mock(ClientDto.class);
        ClientDto created = mock(ClientDto.class);
        when(clientService.createClient(input)).thenReturn(created);

        ResponseEntity<ClientDto> response = controller.createClient(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(created, response.getBody());
        verify(clientService, times(1)).createClient(input);
    }

    @Test
    void getClientById_shouldReturnOk() {
        long id = 1L;
        ClientDto client = mock(ClientDto.class);
        when(clientService.getClientById(id)).thenReturn(client);

        ResponseEntity<ClientDto> response = controller.getClientById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(client, response.getBody());
        verify(clientService, times(1)).getClientById(id);
    }

    @Test
    void getClientByIdentificacion_shouldReturnOk() {
        String identificacion = "ABC123";
        ClientDto client = mock(ClientDto.class);
        when(clientService.getClientByIdentificacion(identificacion)).thenReturn(client);

        ResponseEntity<ClientDto> response = controller.getClientByIdentificacion(identificacion);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(client, response.getBody());
        verify(clientService, times(1)).getClientByIdentificacion(identificacion);
    }

    @Test
    void getAllClients_withoutEstado_shouldDelegateToGetAllClients() {
        List<ClientDto> clients = List.of(mock(ClientDto.class));
        when(clientService.getAllClients()).thenReturn(clients);

        ResponseEntity<List<ClientDto>> response = controller.getAllClients(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(clients, response.getBody());
        verify(clientService, times(1)).getAllClients();
        verify(clientService, never()).getClientsByEstado(anyBoolean());
    }

    @Test
    void getAllClients_withEstado_shouldDelegateToGetClientsByEstado() {
        boolean estado = true;
        List<ClientDto> clients = List.of(mock(ClientDto.class), mock(ClientDto.class));
        when(clientService.getClientsByEstado(estado)).thenReturn(clients);

        ResponseEntity<List<ClientDto>> response = controller.getAllClients(estado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(clients, response.getBody());
        verify(clientService, times(1)).getClientsByEstado(estado);
        verify(clientService, never()).getAllClients();
    }

    @Test
    void updateClient_shouldReturnOk() {
        long id = 5L;
        ClientUpdateDto updateDto = mock(ClientUpdateDto.class);
        ClientDto updated = mock(ClientDto.class);
        when(clientService.updateClient(id, updateDto)).thenReturn(updated);

        ResponseEntity<ClientDto> response = controller.updateClient(id, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(updated, response.getBody());
        verify(clientService, times(1)).updateClient(id, updateDto);
    }

    @Test
    void deleteClient_shouldReturnNoContent() {
        long id = 7L;
        doNothing().when(clientService).deleteClient(id);

        ResponseEntity<Void> response = controller.deleteClient(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(clientService, times(1)).deleteClient(id);
    }
}