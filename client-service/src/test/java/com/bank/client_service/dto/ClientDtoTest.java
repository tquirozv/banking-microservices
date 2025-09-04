package com.bank.client_service.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClientDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDownValidator() {
        if (factory != null) factory.close();
    }

    @Test
    void constructorAndGetterShouldDefensivelyCopyPersona() {
        PersonDto original = new PersonDto("ID-1", "Alice", "F", 30, "Street 1", "1234567890");
        ClientDto dto = new ClientDto(1L, original, "secret", true);

        // The getter must return a different instance with equal field values
        PersonDto fromGetter = dto.getPersona();
        assertNotNull(fromGetter);
        assertNotSame(original, fromGetter);
        assertEquals(original.getIdentificacion(), fromGetter.getIdentificacion());
        assertEquals(original.getNombre(), fromGetter.getNombre());

        // Mutating the returned instance should not affect internal state
        fromGetter.setNombre("Changed");
        assertNotEquals("Changed", dto.getPersona().getNombre());

        // Mutating the original after construction should not affect dto
        original.setNombre("Mutated after ctor");
        assertNotEquals("Mutated after ctor", dto.getPersona().getNombre());
    }

    @Test
    void setterShouldDefensivelyCopyPersona() {
        ClientDto dto = new ClientDto();
        PersonDto external = new PersonDto("ID-2", "Bob", "M", 25, "Street 2", "0987654321");

        dto.setPersona(external);

        PersonDto internal = dto.getPersona();
        assertNotNull(internal);
        assertNotSame(external, internal);
        assertEquals(external.getIdentificacion(), internal.getIdentificacion());

        // Mutating external should not affect internal
        external.setNombre("External change");
        assertNotEquals("External change", dto.getPersona().getNombre());
    }

    @Test
    void validation_shouldFailWhenPersonaIsNull() {
        ClientDto dto = new ClientDto();
        dto.setContrasena("pw");
        dto.setEstado(true);

        Set<ConstraintViolation<ClientDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v ->
                "persona".equals(v.getPropertyPath().toString()) &&
                        "Person data is required".equals(v.getMessage())));
    }

    @Test
    void validation_shouldFailWhenPasswordBlankOrTooLong() {
        ClientDto blank = new ClientDto();
        blank.setPersona(new PersonDto("ID", "Name", "M", 20, "Addr", "1234567890"));
        blank.setContrasena("  ");
        blank.setEstado(true);

        Set<ConstraintViolation<ClientDto>> v1 = validator.validate(blank);
        assertTrue(v1.stream().anyMatch(v -> v.getPropertyPath().toString().equals("contrasena")
                && v.getMessage().equals("Password is required")));

        ClientDto tooLong = new ClientDto();
        tooLong.setPersona(new PersonDto("ID", "Name", "F", 22, "Addr", "1234567890"));
        tooLong.setEstado(false);
        tooLong.setContrasena("x".repeat(256)); // > 255

        Set<ConstraintViolation<ClientDto>> v2 = validator.validate(tooLong);
        assertTrue(v2.stream().anyMatch(v -> v.getPropertyPath().toString().equals("contrasena")
                && v.getMessage().equals("Password must be at most 255 characters")));
    }

    @Test
    void validation_shouldFailWhenEstadoIsNull() {
        ClientDto dto = new ClientDto();
        dto.setPersona(new PersonDto("ID", "Name", "M", 21, "Addr", "1234567890"));
        dto.setContrasena("pw");
        dto.setEstado(null);

        Set<ConstraintViolation<ClientDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v ->
                "estado".equals(v.getPropertyPath().toString()) &&
                        "Status is required".equals(v.getMessage())));
    }

    @Test
    void validation_shouldCascadeToPersona() {
        // Invalid person: missing identificacion and nombre
        PersonDto person = new PersonDto();
        person.setGenero("X"); // invalid pattern
        person.setEdad(-1);    // invalid min
        person.setTelefono("abc"); // invalid pattern

        ClientDto dto = new ClientDto();
        dto.setPersona(person);
        dto.setContrasena("pw");
        dto.setEstado(true);

        Set<ConstraintViolation<ClientDto>> violations = validator.validate(dto);

        // Expect cascaded violations with property path starting with persona.
        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("persona.identificacion") &&
                        v.getMessage().equals("Identification is required")));

        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("persona.nombre") &&
                        v.getMessage().equals("Name is required")));

        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("persona.genero") &&
                        v.getMessage().equals("Gender must be M or F")));

        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("persona.edad") &&
                        v.getMessage().equals("Age must be positive")));

        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("persona.telefono") &&
                        v.getMessage().equals("Invalid phone format")));
    }
}