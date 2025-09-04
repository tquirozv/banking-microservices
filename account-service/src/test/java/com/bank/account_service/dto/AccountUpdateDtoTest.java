package com.bank.account_service.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountUpdateDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenEstadoIsValid_thenNoValidationErrors() {
        AccountUpdateDto dto = new AccountUpdateDto();
        dto.setEstado(true);

        var violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenEstadoIsNull_thenValidationError() {
        AccountUpdateDto dto = new AccountUpdateDto();
        dto.setEstado(null);

        var violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }

    @Test
    void testEquals() {
        AccountUpdateDto dto1 = new AccountUpdateDto();
        dto1.setEstado(true);

        AccountUpdateDto dto2 = new AccountUpdateDto();
        dto2.setEstado(true);

        // Test equality with identical object
        assertThat(dto1).isEqualTo(dto2);

        // Test equality with self
        assertThat(dto1).isEqualTo(dto1);

        // Test inequality with null
        assertThat(dto1).isNotEqualTo(null);

        // Test inequality with different type
        assertThat(dto1).isNotEqualTo(new Object());

        // Test inequality with different values
        AccountUpdateDto differentDto = new AccountUpdateDto();
        differentDto.setEstado(false);

        assertThat(dto1).isNotEqualTo(differentDto);
    }

    @Test
    void testHashCode() {
        AccountUpdateDto dto1 = new AccountUpdateDto();
        dto1.setEstado(true);

        AccountUpdateDto dto2 = new AccountUpdateDto();
        dto2.setEstado(true);

        // Equal objects should have equal hash codes
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());

        // Different objects should have different hash codes
        AccountUpdateDto differentDto = new AccountUpdateDto();
        differentDto.setEstado(false);

        assertThat(dto1.hashCode()).isNotEqualTo(differentDto.hashCode());
    }

    @Test
    void testToString() {
        AccountUpdateDto dto = new AccountUpdateDto();
        dto.setEstado(true);

        String toString = dto.toString();

        assertThat(toString)
                .contains("AccountUpdateDto")
                .contains("estado=true");
    }

    @Test
    void whenFieldIsNull_thenGetterReturnsNull() {
        AccountUpdateDto dto = new AccountUpdateDto();
        assertThat(dto.getEstado()).isNull();
    }

}
