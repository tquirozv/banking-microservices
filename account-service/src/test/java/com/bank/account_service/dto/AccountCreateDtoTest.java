package com.bank.account_service.dto;

import com.bank.account_service.entity.Account.AccountType;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class AccountCreateDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsAreValid_thenNoValidationErrors() {
        AccountCreateDto dto = new AccountCreateDto();
        dto.setNumeroCuenta("123456");
        dto.setTipoCuenta(AccountType.AHORRO);
        dto.setSaldoInicial(BigDecimal.valueOf(100.00));
        dto.setClienteId(1L);

        Set<jakarta.validation.ConstraintViolation<AccountCreateDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideInvalidAccountData")
    void whenFieldsAreInvalid_thenValidationErrors(String numeroCuenta, AccountType tipoCuenta,
            BigDecimal saldoInicial, Long clienteId, int expectedViolations) {
        AccountCreateDto dto = new AccountCreateDto();
        dto.setNumeroCuenta(numeroCuenta);
        dto.setTipoCuenta(tipoCuenta);
        dto.setSaldoInicial(saldoInicial);
        dto.setClienteId(clienteId);

        Set<jakarta.validation.ConstraintViolation<AccountCreateDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(expectedViolations);
    }

    private static Stream<Arguments> provideInvalidAccountData() {
        return Stream.of(
            Arguments.of(null, AccountType.AHORRO, BigDecimal.ONE, 1L, 1), // null account number
            Arguments.of("", AccountType.AHORRO, BigDecimal.ONE, 1L, 1), // empty account number
            Arguments.of("123456", null, BigDecimal.ONE, 1L, 1), // null account type
            Arguments.of("123456", AccountType.AHORRO, null, 1L, 1), // null initial balance
            Arguments.of("123456", AccountType.AHORRO, BigDecimal.valueOf(-100), 1L, 1), // negative balance
            Arguments.of("123456", AccountType.AHORRO, BigDecimal.ONE, null, 1), // null client id
            Arguments.of(null, null, null, null, 4) // all fields null
        );
    }

    @Test
    void testEquals() {
        AccountCreateDto dto1 = new AccountCreateDto();
        dto1.setNumeroCuenta("123456");
        dto1.setTipoCuenta(AccountType.AHORRO);
        dto1.setSaldoInicial(BigDecimal.valueOf(100.00));
        dto1.setClienteId(1L);

        AccountCreateDto dto2 = new AccountCreateDto();
        dto2.setNumeroCuenta("123456");
        dto2.setTipoCuenta(AccountType.AHORRO);
        dto2.setSaldoInicial(BigDecimal.valueOf(100.00));
        dto2.setClienteId(1L);

        // Test equality with identical object
        assertThat(dto1).isEqualTo(dto2);

        // Test equality with self
        assertThat(dto1).isEqualTo(dto1);

        // Test inequality with null
        assertThat(dto1).isNotEqualTo(null);

        // Test inequality with different type
        assertThat(dto1).isNotEqualTo(new Object());

        // Test inequality with different values
        AccountCreateDto differentDto = new AccountCreateDto();
        differentDto.setNumeroCuenta("654321");
        differentDto.setTipoCuenta(AccountType.CORRIENTE);
        differentDto.setSaldoInicial(BigDecimal.valueOf(200.00));
        differentDto.setClienteId(2L);

        assertThat(dto1).isNotEqualTo(differentDto);
    }

    @Test
    void testHashCode() {
        AccountCreateDto dto1 = new AccountCreateDto();
        dto1.setNumeroCuenta("123456");
        dto1.setTipoCuenta(AccountType.AHORRO);
        dto1.setSaldoInicial(BigDecimal.valueOf(100.00));
        dto1.setClienteId(1L);

        AccountCreateDto dto2 = new AccountCreateDto();
        dto2.setNumeroCuenta("123456");
        dto2.setTipoCuenta(AccountType.AHORRO);
        dto2.setSaldoInicial(BigDecimal.valueOf(100.00));
        dto2.setClienteId(1L);

        // Equal objects should have equal hash codes
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());

        // Different objects should have different hash codes
        AccountCreateDto differentDto = new AccountCreateDto();
        differentDto.setNumeroCuenta("654321");
        differentDto.setTipoCuenta(AccountType.CORRIENTE);
        differentDto.setSaldoInicial(BigDecimal.valueOf(200.00));
        differentDto.setClienteId(2L);

        assertThat(dto1.hashCode()).isNotEqualTo(differentDto.hashCode());
    }

    @Test
    void testToString() {
        AccountCreateDto dto = new AccountCreateDto();
        dto.setNumeroCuenta("123456");
        dto.setTipoCuenta(AccountType.AHORRO);
        dto.setSaldoInicial(BigDecimal.valueOf(100.00));
        dto.setClienteId(1L);

        String toString = dto.toString();

        assertThat(toString)
                .contains("AccountCreateDto")
                .contains("numeroCuenta=123456")
                .contains("tipoCuenta=AHORRO")
                .contains("saldoInicial=100.0")
                .contains("clienteId=1");
    }

}
