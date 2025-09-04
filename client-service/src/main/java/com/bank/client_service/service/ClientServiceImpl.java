package com.bank.client_service.service;

import com.bank.client_service.dto.ClientDto;
import com.bank.client_service.dto.ClientUpdateDto;
import com.bank.client_service.dto.PersonDto;
import com.bank.client_service.entity.Client;
import com.bank.client_service.entity.Person;
import com.bank.client_service.exception.ClientNotFoundException;
import com.bank.client_service.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        String identificacion = clientDto.getPersona() != null ? clientDto.getPersona().getIdentificacion() : null;
        log.info("Creating client with identification: {}", identificacion);

        if (clientDto.getPersona() == null) {
            throw new IllegalArgumentException("Person data is required");
        }
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("Identification is required");
        }

        if (clientRepository.existsByPersonaIdentificacion(identificacion)){
            throw new IllegalArgumentException("Client with identification " + identificacion + " already exists");
        }

        Client client = convertToEntity(clientDto);
        client.setContrasena(clientDto.getContrasena());

        Client savedClient = clientRepository.save(client);

        log.info("Client created successfully with ID: {}", savedClient.getClienteid());
        return convertToDto(savedClient);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto getClientById(Long id) {
        log.info("Getting client by ID: {}", id);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with ID: " + id));

        return convertToDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto getClientByIdentificacion(String identificacion) {
        log.info("Getting client by identification: {}", identificacion);

        Client client = clientRepository.findByPersonaIdentificacion(identificacion)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with identification: " + identificacion));

        return convertToDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> getAllClients() {
        log.info("Getting all clients");

        return clientRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> getClientsByEstado(Boolean estado) {
        log.info("Getting clients by status: {}", estado);

        return clientRepository.findByEstado(estado)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDto updateClient(Long id, ClientUpdateDto clientDto) {
        log.info("Updating client with ID: {}", id);

        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with ID: " + id));

        // Defensive getter returns a copy; ensure we set it back after updates
        Person person = existingClient.getPersona();
        if (person == null) {
            person = new Person();
        }

        PersonDto personDto = clientDto.getPersona();
        if (personDto != null) {
            // If creating a new Person or existing identificacion is empty, set identificacion from DTO
            if (personDto.getIdentificacion() != null &&
                    (person.getIdentificacion() == null || person.getIdentificacion().isBlank())) {
                person.setIdentificacion(personDto.getIdentificacion());
            }
            if (personDto.getNombre() != null) {
                person.setNombre(personDto.getNombre());
            }
            if (personDto.getGenero() != null) {
                person.setGenero(personDto.getGenero());
            }
            if (personDto.getEdad() != null) {
                person.setEdad(personDto.getEdad());
            }
            if (personDto.getDireccion() != null) {
                person.setDireccion(personDto.getDireccion());
            }
            if (personDto.getTelefono() != null) {
                person.setTelefono(personDto.getTelefono());
            }
        }

        // Store the possibly updated copy back into the entity
        existingClient.setPersona(person);

        if (clientDto.getContrasena() != null && !clientDto.getContrasena().isBlank()) {
            existingClient.setContrasena(clientDto.getContrasena());
        }
        if (clientDto.getEstado() != null) {
            existingClient.setEstado(clientDto.getEstado());
        }

        Client updatedClient = clientRepository.save(existingClient);

        log.info("Client updated successfully with ID: {}", updatedClient.getClienteid());
        return convertToDto(updatedClient);
    }

    @Override
    public void deleteClient(Long id) {
        log.info("Deleting client with ID: {}", id);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with ID: " + id));

        clientRepository.delete(client);

        log.info("Client deleted successfully with ID: {}", id);
    }

    private Client convertToEntity(ClientDto dto) {
        Client client = new Client();

        PersonDto p = dto.getPersona();
        if (p == null) {
            throw new IllegalArgumentException("Person data is required");
        }

        Person person = new Person();
        person.setIdentificacion(p.getIdentificacion());
        person.setNombre(p.getNombre());
        person.setGenero(p.getGenero());
        person.setEdad(p.getEdad());
        person.setDireccion(p.getDireccion());
        person.setTelefono(p.getTelefono());

        client.setPersona(person);
        client.setContrasena(dto.getContrasena());
        client.setEstado(dto.getEstado());

        return client;
    }

    private ClientDto convertToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setClienteid(client.getClienteid());

        Person person = client.getPersona();
        PersonDto personDto = new PersonDto();
        if (person != null) {
            personDto.setIdentificacion(person.getIdentificacion());
            personDto.setNombre(person.getNombre());
            personDto.setGenero(person.getGenero());
            personDto.setEdad(person.getEdad());
            personDto.setDireccion(person.getDireccion());
            personDto.setTelefono(person.getTelefono());
        }
        dto.setPersona(personDto);
        dto.setEstado(client.getEstado());
        dto.setContrasena(null);
        return dto;
    }
}