package com.bank.client_service.service;

import com.bank.client_service.dto.ClientDto;
import com.bank.client_service.dto.ClientUpdateDto;
import com.bank.client_service.dto.PersonDto;
import com.bank.client_service.entity.Client;
import com.bank.client_service.entity.Person;
import com.bank.client_service.exception.ClientNotFoundException;
import com.bank.client_service.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl service;

    private PersonDto samplePersonDto;
    private ClientDto sampleClientDto;
    private Person samplePersonEntity;
    private Client sampleClientEntity;

    @BeforeEach
    void setup() {
        samplePersonDto = new PersonDto();
        samplePersonDto.setIdentificacion("ID-123");
        samplePersonDto.setNombre("John Doe");
        samplePersonDto.setGenero("M");
        samplePersonDto.setEdad(35);
        samplePersonDto.setDireccion("Main St");
        samplePersonDto.setTelefono("123-456-7890");

        sampleClientDto = new ClientDto();
        sampleClientDto.setPersona(samplePersonDto);
        sampleClientDto.setContrasena("pwd");
        sampleClientDto.setEstado(true);

        samplePersonEntity = new Person();
        samplePersonEntity.setIdentificacion("ID-123");
        samplePersonEntity.setNombre("John Doe");
        samplePersonEntity.setGenero("M");
        samplePersonEntity.setEdad(35);
        samplePersonEntity.setDireccion("Main St");
        samplePersonEntity.setTelefono("123-456-7890");

        sampleClientEntity = new Client();
        sampleClientEntity.setClienteId(10L);
        sampleClientEntity.setPersona(samplePersonEntity);
        sampleClientEntity.setContrasena("pwd");
        sampleClientEntity.setEstado(true);
    }

    @Test
    void createClient_success() {
        when(clientRepository.existsByPersonaIdentificacion("ID-123")).thenReturn(false);
        // Capture the entity passed to save and return a "saved" entity with ID populated
        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        when(clientRepository.save(captor.capture())).thenAnswer(inv -> {
            Client toSave = captor.getValue();
            Client saved = new Client();
            saved.setClienteId(42L);
            saved.setPersona(toSave.getPersona());
            saved.setContrasena(toSave.getContrasena());
            saved.setEstado(toSave.getEstado());
            return saved;
        });

        ClientDto result = service.createClient(sampleClientDto);

        verify(clientRepository).existsByPersonaIdentificacion("ID-123");
        verify(clientRepository).save(any(Client.class));

        assertEquals(42L, result.getClienteId());
        assertNotNull(result.getPersona());
        assertEquals("ID-123", result.getPersona().getIdentificacion());
        assertEquals("John Doe", result.getPersona().getNombre());
        // Service must not expose password in DTO
        assertNull(result.getContrasena());
        assertTrue(result.getEstado());
    }

    @Test
    void createClient_missingPersona_throws() {
        ClientDto dto = new ClientDto();
        dto.setEstado(true);
        dto.setContrasena("pwd");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.createClient(dto));
        assertEquals("Person data is required", ex.getMessage());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void createClient_missingIdentification_throws() {
        ClientDto dto = new ClientDto();
        PersonDto p = new PersonDto();
        p.setIdentificacion("  "); // blank
        dto.setPersona(p);
        dto.setEstado(true);
        dto.setContrasena("pwd");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.createClient(dto));
        assertEquals("Identification is required", ex.getMessage());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void createClient_duplicateIdentification_throws() {
        when(clientRepository.existsByPersonaIdentificacion("ID-123")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.createClient(sampleClientDto));
        assertTrue(ex.getMessage().contains("already exists"));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void getClientById_found() {
        when(clientRepository.findById(10L)).thenReturn(Optional.of(sampleClientEntity));

        ClientDto dto = service.getClientById(10L);

        verify(clientRepository).findById(10L);
        assertEquals(10L, dto.getClienteId());
        assertEquals("ID-123", dto.getPersona().getIdentificacion());
        assertNull(dto.getContrasena()); // hidden by mapping
    }

    @Test
    void getClientById_notFound_throws() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> service.getClientById(999L));
    }

    @Test
    void getClientByIdentificacion_found() {
        when(clientRepository.findByPersonaIdentificacion("ID-123")).thenReturn(Optional.of(sampleClientEntity));

        ClientDto dto = service.getClientByIdentificacion("ID-123");

        verify(clientRepository).findByPersonaIdentificacion("ID-123");
        assertEquals("ID-123", dto.getPersona().getIdentificacion());
        assertNull(dto.getContrasena());
    }

    @Test
    void getClientByIdentificacion_notFound_throws() {
        when(clientRepository.findByPersonaIdentificacion("NOPE")).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> service.getClientByIdentificacion("NOPE"));
    }

    @Test
    void getAllClients_mapsEntitiesToDtos() {
        Client c1 = cloneClientWithId(sampleClientEntity, 1L);
        Client c2 = cloneClientWithId(sampleClientEntity, 2L);
        when(clientRepository.findAll()).thenReturn(List.of(c1, c2));

        List<ClientDto> result = service.getAllClients();

        verify(clientRepository).findAll();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getClienteId());
        assertEquals(2L, result.get(1).getClienteId());
        assertNull(result.get(0).getContrasena());
        assertNull(result.get(1).getContrasena());
    }

    @Test
    void getClientsByEstado_mapsEntitiesToDtos() {
        Client active = cloneClientWithId(sampleClientEntity, 3L);
        active.setEstado(true);
        when(clientRepository.findByEstado(true)).thenReturn(List.of(active));

        List<ClientDto> result = service.getClientsByEstado(true);

        verify(clientRepository).findByEstado(true);
        assertEquals(1, result.size());
        assertEquals(3L, result.getFirst().getClienteId());
        assertTrue(result.getFirst().getEstado());
    }

    @Test
    void updateClient_shouldUpdateSelectedFields_andIgnoreBlankPassword() {
        // Existing client with a person
        Client existing = new Client();
        existing.setClienteId(50L);
        Person existingPerson = new Person();
        existingPerson.setIdentificacion("ID-123");
        existingPerson.setNombre("Old Name");
        existingPerson.setGenero("M");
        existingPerson.setEdad(30);
        existingPerson.setDireccion("Old Addr");
        existingPerson.setTelefono("000-000-0000");
        existing.setPersona(existingPerson);
        existing.setContrasena("oldpwd");
        existing.setEstado(false);

        when(clientRepository.findById(50L)).thenReturn(Optional.of(existing));
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0, Client.class));

        // DTO with partial updates: change nombre, edad, telefono; blank password should be ignored; estado -> true
        PersonDto updatePerson = new PersonDto();
        updatePerson.setNombre("New Name");
        updatePerson.setEdad(31);
        updatePerson.setTelefono("111-111-1111");
        ClientUpdateDto updateDto = new ClientUpdateDto();
        updateDto.setPersona(updatePerson);
        updateDto.setContrasena("   "); // should be ignored
        updateDto.setEstado(true);

        ClientDto result = service.updateClient(50L, updateDto);

        verify(clientRepository).findById(50L);
        verify(clientRepository).save(any(Client.class));

        // Expect selective updates applied
        assertEquals(50L, result.getClienteId());
        assertEquals("New Name", result.getPersona().getNombre());
        assertEquals(31, result.getPersona().getEdad());
        assertEquals("111-111-1111", result.getPersona().getTelefono());
        // Unchanged fields remain
        assertEquals("M", result.getPersona().getGenero());
        assertEquals("Old Addr", result.getPersona().getDireccion());
        // Password hidden in DTO
        assertNull(result.getContrasena());
        assertTrue(result.getEstado());
    }

    @Test
    void updateClient_whenExistingHasNullPerson_shouldCreateAndPopulatePerson() {
        Client existing = new Client();
        existing.setClienteId(77L);
        existing.setPersona(null);
        existing.setContrasena("oldpwd");
        existing.setEstado(false);

        when(clientRepository.findById(77L)).thenReturn(Optional.of(existing));
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0, Client.class));

        PersonDto p = new PersonDto();
        p.setIdentificacion("NEW-ID");
        p.setNombre("Jane");
        p.setGenero("F");
        p.setEdad(28);
        p.setDireccion("New Addr");
        p.setTelefono("222-222-2222");
        ClientUpdateDto update = new ClientUpdateDto();
        update.setPersona(p);
        update.setEstado(true);

        ClientDto result = service.updateClient(77L, update);

        verify(clientRepository).findById(77L);
        verify(clientRepository).save(any(Client.class));

        assertEquals(77L, result.getClienteId());
        assertEquals("NEW-ID", result.getPersona().getIdentificacion());
        assertEquals("Jane", result.getPersona().getNombre());
        assertEquals("F", result.getPersona().getGenero());
        assertEquals(28, result.getPersona().getEdad());
        assertEquals("New Addr", result.getPersona().getDireccion());
        assertEquals("222-222-2222", result.getPersona().getTelefono());
        assertTrue(result.getEstado());
        assertNull(result.getContrasena());
    }

    @Test
    void updateClient_notFound_throws() {
        when(clientRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> service.updateClient(404L, new ClientUpdateDto()));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void deleteClient_found_deletes() {
        when(clientRepository.findById(10L)).thenReturn(Optional.of(sampleClientEntity));

        service.deleteClient(10L);

        verify(clientRepository).findById(10L);
        verify(clientRepository).delete(sampleClientEntity);
    }

    @Test
    void deleteClient_notFound_throws() {
        when(clientRepository.findById(11L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> service.deleteClient(11L));
        verify(clientRepository, never()).delete(any());
    }

    private static Client cloneClientWithId(Client base, Long id) {
        Person p = new Person();
        if (base.getPersona() != null) {
            Person bp = base.getPersona();
            p.setIdentificacion(bp.getIdentificacion());
            p.setNombre(bp.getNombre());
            p.setGenero(bp.getGenero());
            p.setEdad(bp.getEdad());
            p.setDireccion(bp.getDireccion());
            p.setTelefono(bp.getTelefono());
        }
        Client c = new Client();
        c.setClienteId(id);
        c.setPersona(p);
        c.setContrasena(base.getContrasena());
        c.setEstado(base.getEstado());
        return c;
    }
}
