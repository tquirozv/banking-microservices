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

class PersonDtoTest {

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
    void validation_shouldPassForValidPerson() {
        PersonDto p = new PersonDto("ABC123", "John Doe", "M", 40, "Main St", "123-456-7890");
        Set<ConstraintViolation<PersonDto>> violations = validator.validate(p);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_shouldReportExpectedViolations() {
        PersonDto p = new PersonDto();
        p.setIdentificacion(""); // NotBlank
        p.setNombre(" ");        // NotBlank
        p.setGenero("X");        // Pattern
        p.setEdad(-5);           // Min
        p.setDireccion("x".repeat(501)); // Size
        p.setTelefono("bad-phone");       // Pattern

        Set<ConstraintViolation<PersonDto>> v = validator.validate(p);

        assertTrue(v.stream().anyMatch(cv ->
                cv.getPropertyPath().toString().equals("identificacion") &&
                        cv.getMessage().equals("Identification is required")));

        assertTrue(v.stream().anyMatch(cv ->
                cv.getPropertyPath().toString().equals("nombre") &&
                        cv.getMessage().equals("Name is required")));

        assertTrue(v.stream().anyMatch(cv ->
                cv.getPropertyPath().toString().equals("genero") &&
                        cv.getMessage().equals("Gender must be M or F")));

        assertTrue(v.stream().anyMatch(cv ->
                cv.getPropertyPath().toString().equals("edad") &&
                        cv.getMessage().equals("Age must be positive")));

        assertTrue(v.stream().anyMatch(cv ->
                cv.getPropertyPath().toString().equals("direccion") &&
                        cv.getMessage().equals("Address must be at most 500 characters")));

        assertTrue(v.stream().anyMatch(cv ->
                cv.getPropertyPath().toString().equals("telefono") &&
                        cv.getMessage().equals("Invalid phone format")));
    }

    @Test
    void copyConstructor_shouldCreateIndependentCopy() {
        PersonDto original = new PersonDto("ID-9", "Carol", "F", 28, "Somewhere", "1112223333");
        PersonDto copy = new PersonDto(original);

        assertNotSame(original, copy);
        assertEquals(original.getIdentificacion(), copy.getIdentificacion());
        assertEquals(original.getNombre(), copy.getNombre());
        assertEquals(original.getGenero(), copy.getGenero());
        assertEquals(original.getEdad(), copy.getEdad());
        assertEquals(original.getDireccion(), copy.getDireccion());
        assertEquals(original.getTelefono(), copy.getTelefono());

        // Mutate original; copy should remain unchanged
        original.setNombre("Changed");
        assertNotEquals(original.getNombre(), copy.getNombre());
    }
}