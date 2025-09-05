package com.bank.account_service.utils;

import com.bank.account_service.dto.*;
import com.bank.account_service.entity.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ToDtoTest {

    @Test
    void testMovementConvertToDto() {
        Movement movement = new Movement();
        movement.setId(1L);
        movement.setNumeroCuenta("123456");
        movement.setFecha(LocalDateTime.now());
        movement.setTipoMovimiento(Movement.MovementType.CREDITO);
        movement.setValor(BigDecimal.valueOf(1000));
        movement.setSaldo(BigDecimal.valueOf(5000));
        movement.setDescripcion("Test movement");
        movement.setCreatedAt(LocalDateTime.now());

        MovementResponseDto dto = ToDto.movementConvertToDto(movement);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getCuentaId()).isEqualTo("123456");
        assertThat(dto.getTipoMovimiento()).isEqualTo(Movement.MovementType.CREDITO);
        assertThat(dto.getValor()).isEqualByComparingTo("1000");
        assertThat(dto.getSaldo()).isEqualByComparingTo("5000");
        assertThat(dto.getDescripcion()).isEqualTo("Test movement");
    }

    @Test
    void testAccountConvertToDto() {
        Account account = new Account();
        account.setNumeroCuenta("ACC123");
        account.setTipoCuenta(Account.AccountType.AHORRO);
        account.setSaldoInicial(BigDecimal.valueOf(2000));
        account.setSaldoActual(BigDecimal.valueOf(1500));
        account.setEstado(true);
        account.setClienteId(99L);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        AccountResponseDto dto = ToDto.accountConvertToDto(account);

        assertThat(dto).isNotNull();
        assertThat(dto.getNumeroCuenta()).isEqualTo("ACC123");
        assertThat(dto.getTipoCuenta()).isEqualTo(Account.AccountType.AHORRO);
        assertThat(dto.getSaldoInicial()).isEqualByComparingTo("2000");
        assertThat(dto.getSaldoActual()).isEqualByComparingTo("1500");
        assertThat(dto.getEstado()).isTrue();
        assertThat(dto.getClienteId()).isEqualTo(99L);
    }

    @Test
    void testAccountWithMovementsConvertToDto() {
        Movement movement = new Movement();
        movement.setId(1L);
        movement.setNumeroCuenta("ACC123");
        movement.setFecha(LocalDateTime.now());
        movement.setTipoMovimiento(Movement.MovementType.DEBITO);
        movement.setValor(BigDecimal.valueOf(500));
        movement.setSaldo(BigDecimal.valueOf(1000));
        movement.setDescripcion("Withdrawal");
        movement.setCreatedAt(LocalDateTime.now());

        Account account = new Account();
        account.setNumeroCuenta("ACC123");
        account.setTipoCuenta(Account.AccountType.CORRIENTE);
        account.setSaldoInicial(BigDecimal.valueOf(3000));
        account.setSaldoActual(BigDecimal.valueOf(2500));
        account.setEstado(true);
        account.setClienteId(88L);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        account.setMovimientos(List.of(movement));

        AccountWithMovementsDto dto = ToDto.accountWithMovementsConvertToDto(account);

        assertThat(dto).isNotNull();
        assertThat(dto.getMovements()).hasSize(1);
        assertThat(dto.getMovements().getFirst().getDescripcion()).isEqualTo("Withdrawal");
    }

    @Test
    void testReportCreation() {
        ClientDto client = new ClientDto();
        client.setClienteId(101L);
        PersonDto person = new PersonDto();
        person.setNombre("Thomas");
        person.setIdentificacion("ABC123");
        client.setPersona(person);

        MovementResponseDto movement = new MovementResponseDto();
        movement.setFecha(LocalDateTime.now());
        movement.setTipoMovimiento(Movement.MovementType.CREDITO);
        movement.setValor(BigDecimal.valueOf(100));
        movement.setSaldo(BigDecimal.valueOf(1100));
        movement.setCreatedAt(LocalDateTime.now());

        AccountWithMovementsDto account = new AccountWithMovementsDto();
        account.setNumeroCuenta("ACC999");
        account.setTipoCuenta(Account.AccountType.AHORRO);
        account.setSaldoInicial(BigDecimal.valueOf(1000));
        account.setSaldoActual(BigDecimal.valueOf(1100));
        account.setMovements(List.of(movement));

        List<ReportDto> reports = ToDto.reportCreation(client, List.of(account));

        assertThat(reports).hasSize(1);
        ReportDto report = reports.getFirst();
        assertThat(report.getNombre()).isEqualTo("Thomas");
        assertThat(report.getNumeroCuenta()).isEqualTo("ACC999");
        assertThat(report.getTipoMovimiento()).isEqualTo(Movement.MovementType.CREDITO);
    }
}
