package com.bank.account_service.dto;

import com.bank.account_service.entity.Movement.MovementType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MovementCreateDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsAreValid_thenNoValidationErrors() {
        MovementCreateDto dto = new MovementCreateDto();
        dto.setCuentaId("123456");
        dto.setTipoMovimiento(MovementType.CREDITO);
        dto.setValor(BigDecimal.valueOf(100.00));
        dto.setDescripcion("Test movement");

        Set<ConstraintViolation<MovementCreateDto>> violations = validator.validate(dto);
        
        assertThat(violations).isEmpty();
        assertThat(dto.getCuentaId()).isEqualTo("123456");
        assertThat(dto.getTipoMovimiento()).isEqualTo(MovementType.CREDITO);
        assertThat(dto.getValor()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(dto.getDescripcion()).isEqualTo("Test movement");
    }

    @Test
    void whenDescripcionIsNull_thenNoValidationErrors() {
        MovementCreateDto dto = new MovementCreateDto();
        dto.setCuentaId("123456");
        dto.setTipoMovimiento(MovementType.CREDITO);
        dto.setValor(BigDecimal.valueOf(100.00));
        dto.setDescripcion(null);

        Set<ConstraintViolation<MovementCreateDto>> violations = validator.validate(dto);
        
        assertThat(violations).isEmpty();
        assertThat(dto.getDescripcion()).isNull();
    }

    @Test
    void whenCuentaIdIsNull_thenValidationError() {
        MovementCreateDto dto = new MovementCreateDto();
        dto.setCuentaId(null);
        dto.setTipoMovimiento(MovementType.CREDITO);
        dto.setValor(BigDecimal.valueOf(100.00));
        dto.setDescripcion("Test movement");

        Set<ConstraintViolation<MovementCreateDto>> violations = validator.validate(dto);
        
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Account ID is required");
    }

    @Test
    void whenTipoMovimientoIsNull_thenValidationError() {
        MovementCreateDto dto = new MovementCreateDto();
        dto.setCuentaId("123456");
        dto.setTipoMovimiento(null);
        dto.setValor(BigDecimal.valueOf(100.00));
        dto.setDescripcion("Test movement");

        Set<ConstraintViolation<MovementCreateDto>> violations = validator.validate(dto);
        
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Movement type is required");
    }

    @Test
    void whenValorIsNull_thenValidationError() {
        MovementCreateDto dto = new MovementCreateDto();
        dto.setCuentaId("123456");
        dto.setTipoMovimiento(MovementType.CREDITO);
        dto.setValor(null);
        dto.setDescripcion("Test movement");

        Set<ConstraintViolation<MovementCreateDto>> violations = validator.validate(dto);
        
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Amount is required");
    }

    @Test
    void whenValorIsNegative_thenValidationError() {
        MovementCreateDto dto = new MovementCreateDto();
        dto.setCuentaId("123456");
        dto.setTipoMovimiento(MovementType.CREDITO);
        dto.setValor(BigDecimal.valueOf(-100.00));
        dto.setDescripcion("Test movement");

        Set<ConstraintViolation<MovementCreateDto>> violations = validator.validate(dto);
        
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Amount must be positive");
    }

    @Test
    void whenValorIsZero_thenValidationError() {
        MovementCreateDto dto = new MovementCreateDto();
        dto.setCuentaId("123456");
        dto.setTipoMovimiento(MovementType.CREDITO);
        dto.setValor(BigDecimal.ZERO);
        dto.setDescripcion("Test movement");

        Set<ConstraintViolation<MovementCreateDto>> violations = validator.validate(dto);
        
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Amount must be positive");
    }

    @Test
    void whenMultipleFieldsAreInvalid_thenMultipleValidationErrors() {
        MovementCreateDto dto = new MovementCreateDto();
        dto.setCuentaId(null);
        dto.setTipoMovimiento(null);
        dto.setValor(null);
        dto.setDescripcion("Test movement");

        Set<ConstraintViolation<MovementCreateDto>> violations = validator.validate(dto);
        
        assertThat(violations).hasSize(3);
    }

    @Test
    void testEqualsAndHashCode() {
        MovementCreateDto dto1 = new MovementCreateDto();
        dto1.setCuentaId("123456");
        dto1.setTipoMovimiento(MovementType.CREDITO);
        dto1.setValor(BigDecimal.valueOf(100.00));
        dto1.setDescripcion("Test");

        MovementCreateDto dto2 = new MovementCreateDto();
        dto2.setCuentaId("123456");
        dto2.setTipoMovimiento(MovementType.CREDITO);
        dto2.setValor(BigDecimal.valueOf(100.00));
        dto2.setDescripcion("Test");

        MovementCreateDto dto3 = new MovementCreateDto();
        dto3.setCuentaId("23456");
        dto3.setTipoMovimiento(MovementType.DEBITO);
        dto3.setValor(BigDecimal.valueOf(200.00));
        dto3.setDescripcion("Different");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1).isNotEqualTo(dto3);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        MovementCreateDto dto = new MovementCreateDto();
        dto.setCuentaId("123456");
        dto.setTipoMovimiento(MovementType.CREDITO);
        dto.setValor(BigDecimal.valueOf(100.00));
        dto.setDescripcion("Test movement");

        String toString = dto.toString();
        
        assertThat(toString).contains("MovementCreateDto");
        assertThat(toString).contains("cuentaId=1");
        assertThat(toString).contains("tipoMovimiento=CREDITO");
        assertThat(toString).contains("valor=100");
        assertThat(toString).contains("descripcion=Test movement");
    }

    @Test
    void whenTestingBothMovementTypes_thenBothAreValid() {
        MovementCreateDto creditoDto = new MovementCreateDto();
        creditoDto.setCuentaId("123456");
        creditoDto.setTipoMovimiento(MovementType.CREDITO);
        creditoDto.setValor(BigDecimal.valueOf(100.00));

        MovementCreateDto debitoDto = new MovementCreateDto();
        debitoDto.setCuentaId("123456");
        debitoDto.setTipoMovimiento(MovementType.DEBITO);
        debitoDto.setValor(BigDecimal.valueOf(100.00));

        Set<ConstraintViolation<MovementCreateDto>> creditoViolations = validator.validate(creditoDto);
        Set<ConstraintViolation<MovementCreateDto>> debitoViolations = validator.validate(debitoDto);
        
        assertThat(creditoViolations).isEmpty();
        assertThat(debitoViolations).isEmpty();
    }
}
