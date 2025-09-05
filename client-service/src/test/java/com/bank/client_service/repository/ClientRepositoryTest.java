package com.bank.client_service.repository;

import com.bank.client_service.entity.Client;
import com.bank.client_service.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository repository;

    // Helper to build a Client with an associated Person (not yet persisted)
    private static Client newClient(String identificacion, String nombre, boolean estado) {
        Person p = new Person();
        p.setIdentificacion(identificacion);
        p.setNombre(nombre);
        p.setGenero("M");
        p.setEdad(30);
        p.setDireccion("Somewhere");
        p.setTelefono("123-456-7890");

        Client c = new Client();
        c.setPersona(p);           // cascade ALL on Client.persona will persist Person
        c.setContrasena("pwd");
        c.setEstado(estado);
        return c;
    }

    @Test
    void findByPersonaIdentificacion_shouldReturnMatch() {
        repository.saveAndFlush(newClient("ID-100", "Alice Johnson", true));

        Optional<Client> found = repository.findByPersonaIdentificacion("ID-100");

        assertTrue(found.isPresent());
        assertNotNull(found.get().getClienteId());
        assertEquals("ID-100", found.get().getPersona().getIdentificacion());
        assertEquals("Alice Johnson", found.get().getPersona().getNombre());
    }

    @Test
    void existsByPersonaIdentificacion_shouldReturnTrueWhenExists() {
        repository.saveAndFlush(newClient("ID-200", "Bob Smith", false));

        boolean exists = repository.existsByPersonaIdentificacion("ID-200");

        assertTrue(exists);
    }

    @Test
    void existsByPersonaIdentificacion_shouldReturnFalseWhenNotExists() {
        boolean exists = repository.existsByPersonaIdentificacion("NOPE");
        assertFalse(exists);
    }

    @Test
    void findByEstado_shouldFilterByStatus() {
        repository.saveAllAndFlush(List.of(
                newClient("ID-300", "User A", true),
                newClient("ID-301", "User B", false),
                newClient("ID-302", "User C", true)
        ));

        List<Client> active = repository.findByEstado(true);
        List<Client> inactive = repository.findByEstado(false);

        assertEquals(2, active.size());
        assertTrue(active.stream().allMatch(c -> Boolean.TRUE.equals(c.getEstado())));

        assertEquals(1, inactive.size());
        assertTrue(inactive.stream().allMatch(c -> Boolean.FALSE.equals(c.getEstado())));
    }

    @Test
    void findByPersonaNombreContainingIgnoreCase_shouldMatchSubstringIgnoringCase() {
        repository.saveAllAndFlush(List.of(
                newClient("ID-400", "Alice Johnson", true),
                newClient("ID-401", "Bob Smith", true),
                newClient("ID-402", "ALINA ROSE", true)
        ));

        List<Client> result1 = repository.findByPersonaNombreContainingIgnoreCase("john");
        assertEquals(1, result1.size());
        assertEquals("Alice Johnson", result1.getFirst().getPersona().getNombre());

        List<Client> result2 = repository.findByPersonaNombreContainingIgnoreCase("ali");
        // Matches "Alice Johnson" and "ALINA ROSE"
        assertEquals(2, result2.size());
        assertTrue(result2.stream().anyMatch(c -> "Alice Johnson".equals(c.getPersona().getNombre())));
        assertTrue(result2.stream().anyMatch(c -> "ALINA ROSE".equals(c.getPersona().getNombre())));
    }

    @Test
    void findByPersonaNombreStartingWithIgnoreCase_shouldMatchPrefixIgnoringCase() {
        repository.saveAllAndFlush(List.of(
                newClient("ID-500", "Carlos", true),
                newClient("ID-501", "carolina", true),
                newClient("ID-502", "David", true)
        ));

        List<Client> result = repository.findByPersonaNombreStartingWithIgnoreCase("car");
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> "Carlos".equals(c.getPersona().getNombre())));
        assertTrue(result.stream().anyMatch(c -> "carolina".equals(c.getPersona().getNombre())));
    }
}
