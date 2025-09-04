package com.bank.account_service.dto;

import com.bank.account_service.entity.Account.AccountType;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AccountResponseDtoTest {

    @Test
    void whenAllFieldsAreSet_thenGettersReturnCorrectValues() {
        AccountResponseDto dto = new AccountResponseDto();
        LocalDateTime now = LocalDateTime.now();

        dto.setId(1L);
        dto.setNumeroCuenta("123456");
        dto.setTipoCuenta(AccountType.AHORRO);
        dto.setSaldoInicial(BigDecimal.valueOf(100.00));
        dto.setSaldoActual(BigDecimal.valueOf(150.00));
        dto.setEstado(true);
        dto.setClienteId(1L);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNumeroCuenta()).isEqualTo("123456");
        assertThat(dto.getTipoCuenta()).isEqualTo(AccountType.AHORRO);
        assertThat(dto.getSaldoInicial()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(dto.getSaldoActual()).isEqualByComparingTo(BigDecimal.valueOf(150.00));
        assertThat(dto.getEstado()).isTrue();
        assertThat(dto.getClienteId()).isEqualTo(1L);
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        
        AccountResponseDto dto1 = new AccountResponseDto();
        dto1.setId(1L);
        dto1.setNumeroCuenta("123456");
        dto1.setTipoCuenta(AccountType.AHORRO);
        dto1.setCreatedAt(now);

        AccountResponseDto dto2 = new AccountResponseDto();
        dto2.setId(1L);
        dto2.setNumeroCuenta("123456");
        dto2.setTipoCuenta(AccountType.AHORRO);
        dto2.setCreatedAt(now);

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setId(1L);
        dto.setNumeroCuenta("123456");

        String toString = dto.toString();
        
        assertThat(toString).contains("AccountResponseDto");
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("numeroCuenta=123456");
    }

    @Test
    void whenFieldsAreNull_thenGettersReturnNull() {
        AccountResponseDto dto = new AccountResponseDto();

        assertThat(dto.getId()).isNull();
        assertThat(dto.getNumeroCuenta()).isNull();
        assertThat(dto.getTipoCuenta()).isNull();
        assertThat(dto.getSaldoInicial()).isNull();
        assertThat(dto.getSaldoActual()).isNull();
        assertThat(dto.getEstado()).isNull();
        assertThat(dto.getClienteId()).isNull();
        assertThat(dto.getCreatedAt()).isNull();
        assertThat(dto.getUpdatedAt()).isNull();
    }
}