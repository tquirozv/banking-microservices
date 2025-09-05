package com.bank.client_service.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private static Person newPerson(String id, String name) {
        return new Person(
                id,
                name,
                "M",
                30,
                "Some Address",
                "1234567890",
                LocalDateTime.of(2023, 1, 1, 0, 0),
                LocalDateTime.of(2023, 1, 2, 0, 0)
        );
    }

    @Test
    void defaultConstructor_shouldInitializeEstadoTrue() {
        Client client = new Client();
        assertEquals(Boolean.TRUE, client.getEstado(), "Default estado should be TRUE");
    }

    @Test
    void constructor_shouldDefensivelyCopyPersona() {
        Person original = newPerson("ID-1", "Alice");
        Client client = new Client(
                1L,
                original,
                "secret",
                false,
                LocalDateTime.of(2023, 1, 3, 0, 0),
                LocalDateTime.of(2023, 1, 4, 0, 0)
        );

        Person fromGetter1 = client.getPersona();
        assertNotNull(fromGetter1, "Persona from getter should not be null");
        assertNotSame(original, fromGetter1, "Getter must return a different instance");
        assertEquals(original.getIdentificacion(), fromGetter1.getIdentificacion());
        assertEquals(original.getNombre(), fromGetter1.getNombre());
        assertEquals(original.getTelefono(), fromGetter1.getTelefono());

        // Mutate original; internal state must be unaffected
        original.setNombre("Mutated Original");
        Person fromGetter2 = client.getPersona();
        assertNotEquals("Mutated Original", fromGetter2.getNombre(), "Internal state must be isolated from original");

        // Mutate the object returned by getter; internal state must be unaffected
        fromGetter1.setTelefono("0000000000");
        Person fromGetter3 = client.getPersona();
        assertNotEquals("0000000000", fromGetter3.getTelefono(), "Internal state must be isolated from getter copies");

        // Other fields should be set as provided
        assertEquals(1L, client.getClienteId());
        assertEquals("secret", client.getContrasena());
        assertEquals(false, client.getEstado());
    }

    @Test
    void getter_shouldReturnNullWhenPersonaIsNull() {
        Client client = new Client();
        client.setPersona(null);
        assertNull(client.getPersona(), "Getter should return null when internal persona is null");
    }

    @Test
    void setter_shouldDefensivelyCopyPersona() {
        Client client = new Client();
        Person external = newPerson("ID-2", "Bob");

        client.setPersona(external);

        Person fromGetter1 = client.getPersona();
        assertNotNull(fromGetter1);
        assertNotSame(external, fromGetter1, "Setter must store a defensive copy");
        assertEquals(external.getIdentificacion(), fromGetter1.getIdentificacion());
        assertEquals(external.getNombre(), fromGetter1.getNombre());

        // Mutate external after setting; internal state must be unaffected
        external.setNombre("External Changed");
        Person fromGetter2 = client.getPersona();
        assertNotEquals("External Changed", fromGetter2.getNombre(), "Internal state must be isolated from external object");

        // Mutate returned copy; internal state must remain unaffected
        fromGetter1.setNombre("Copy Changed");
        Person fromGetter3 = client.getPersona();
        assertNotEquals("Copy Changed", fromGetter3.getNombre(), "Internal state must be isolated from getter copies");
    }

    @Test
    void setPersona_null_shouldClearPersona() {
        Client client = new Client();
        client.setPersona(newPerson("ID-3", "Carol"));
        assertNotNull(client.getPersona(), "Precondition: persona should be set");

        client.setPersona(null);
        assertNull(client.getPersona(), "Setting persona to null should clear it");
    }
}