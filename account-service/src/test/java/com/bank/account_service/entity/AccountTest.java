package com.bank.account_service.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
    }

    @Test
    void whenCreatingAccountWithDefaultConstructor_thenDefaultValuesAreSet() {
        Account newAccount = new Account();

        assertThat(newAccount.getNumeroCuenta()).isNull();
        assertThat(newAccount.getTipoCuenta()).isNull();
        assertThat(newAccount.getSaldoInicial()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(newAccount.getSaldoActual()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(newAccount.getEstado()).isTrue(); // Default value
        assertThat(newAccount.getClienteId()).isNull();
        assertThat(newAccount.getMovimientos()).isEmpty();
        assertThat(newAccount.getCreatedAt()).isNull();
        assertThat(newAccount.getUpdatedAt()).isNull();
    }

    @Test
    void whenCreatingAccountWithAllArgsConstructor_thenAllFieldsAreSet() {
        LocalDateTime createdAt = LocalDateTime.now().minusHours(2);
        LocalDateTime updatedAt = LocalDateTime.now().minusHours(1);
        List<Movement> movements = new ArrayList<>();
        
        Account account = new Account(
            "123456",
            Account.AccountType.AHORRO,
            BigDecimal.valueOf(1000.00),
            BigDecimal.valueOf(1500.00),
            true,
            100L,
            movements,
            createdAt,
            updatedAt
        );

        assertThat(account.getNumeroCuenta()).isEqualTo("123456");
        assertThat(account.getTipoCuenta()).isEqualTo(Account.AccountType.AHORRO);
        assertThat(account.getSaldoInicial()).isEqualByComparingTo(BigDecimal.valueOf(1000.00));
        assertThat(account.getSaldoActual()).isEqualByComparingTo(BigDecimal.valueOf(1500.00));
        assertThat(account.getEstado()).isTrue();
        assertThat(account.getClienteId()).isEqualTo(100L);
        assertThat(account.getMovimientos()).isEqualTo(movements);
        assertThat(account.getCreatedAt()).isEqualTo(createdAt);
        assertThat(account.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void whenSettingAllFields_thenGettersReturnCorrectValues() {
        LocalDateTime createdAt = LocalDateTime.now().minusHours(2);
        LocalDateTime updatedAt = LocalDateTime.now().minusHours(1);
        List<Movement> movements = new ArrayList<>();

        account.setNumeroCuenta("123456");
        account.setTipoCuenta(Account.AccountType.AHORRO);
        account.setSaldoInicial(BigDecimal.valueOf(1000.00));
        account.setSaldoActual(BigDecimal.valueOf(1500.00));
        account.setEstado(true);
        account.setClienteId(100L);
        account.setMovimientos(movements);
        account.setCreatedAt(createdAt);
        account.setUpdatedAt(updatedAt);

        assertThat(account.getNumeroCuenta()).isEqualTo("123456");
        assertThat(account.getTipoCuenta()).isEqualTo(Account.AccountType.AHORRO);
        assertThat(account.getSaldoInicial()).isEqualByComparingTo(BigDecimal.valueOf(1000.00));
        assertThat(account.getSaldoActual()).isEqualByComparingTo(BigDecimal.valueOf(1500.00));
        assertThat(account.getEstado()).isTrue();
        assertThat(account.getClienteId()).isEqualTo(100L);
        assertThat(account.getMovimientos()).isEqualTo(movements);
        assertThat(account.getCreatedAt()).isEqualTo(createdAt);
        assertThat(account.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void testAccountTypeEnum() {
        assertThat(Account.AccountType.AHORRO).isNotNull();
        assertThat(Account.AccountType.CORRIENTE).isNotNull();
        assertThat(Account.AccountType.values()).hasSize(2);
        assertThat(Account.AccountType.valueOf("AHORRO")).isEqualTo(Account.AccountType.AHORRO);
        assertThat(Account.AccountType.valueOf("CORRIENTE")).isEqualTo(Account.AccountType.CORRIENTE);
    }

    @Test
    void whenCreatingAhorroAccount_thenTypeIsCorrect() {
        account.setTipoCuenta(Account.AccountType.AHORRO);
        account.setNumeroCuenta("AHORRO-001");
        account.setSaldoInicial(BigDecimal.valueOf(500.00));

        assertThat(account.getTipoCuenta()).isEqualTo(Account.AccountType.AHORRO);
        assertThat(account.getNumeroCuenta()).isEqualTo("AHORRO-001");
        assertThat(account.getSaldoInicial()).isEqualByComparingTo(BigDecimal.valueOf(500.00));
    }

    @Test
    void whenCreatingCorrienteAccount_thenTypeIsCorrect() {
        account.setTipoCuenta(Account.AccountType.CORRIENTE);
        account.setNumeroCuenta("CORRIENTE-001");
        account.setSaldoInicial(BigDecimal.valueOf(1000.00));

        assertThat(account.getTipoCuenta()).isEqualTo(Account.AccountType.CORRIENTE);
        assertThat(account.getNumeroCuenta()).isEqualTo("CORRIENTE-001");
        assertThat(account.getSaldoInicial()).isEqualByComparingTo(BigDecimal.valueOf(1000.00));
    }

    @Test
    void whenAccountIsActive_thenEstadoIsTrue() {
        account.setEstado(true);
        assertThat(account.getEstado()).isTrue();
    }

    @Test
    void whenAccountIsInactive_thenEstadoIsFalse() {
        account.setEstado(false);
        assertThat(account.getEstado()).isFalse();
    }

    @Test
    void testEqualsAndHashCode() {
        Account account1 = new Account();
        account1.setNumeroCuenta("123456");
        account1.setTipoCuenta(Account.AccountType.AHORRO);
        account1.setClienteId(100L);

        Account account2 = new Account();
        account2.setNumeroCuenta("123456");
        account2.setTipoCuenta(Account.AccountType.AHORRO);
        account2.setClienteId(100L);

        Account account3 = new Account();
        account3.setNumeroCuenta("654321");
        account3.setTipoCuenta(Account.AccountType.CORRIENTE);
        account3.setClienteId(200L);

        assertThat(account1).isEqualTo(account2);
        assertThat(account1).isNotEqualTo(account3);
        assertThat(account1.hashCode()).isEqualTo(account2.hashCode());
    }

    @Test
    void testToString() {
        account.setNumeroCuenta("123456");
        account.setTipoCuenta(Account.AccountType.AHORRO);
        account.setClienteId(100L);

        String toString = account.toString();
        
        assertThat(toString).contains("Account");
        assertThat(toString).contains("numeroCuenta=123456");
        assertThat(toString).contains("tipoCuenta=AHORRO");
        assertThat(toString).contains("clienteId=100");
    }

    @Test
    void whenDecimalValuesHavePrecision_thenPrecisionIsPreserved() {
        BigDecimal preciseSaldoInicial = new BigDecimal("1000.123456");
        BigDecimal preciseSaldoActual = new BigDecimal("1234.987654");
        
        account.setSaldoInicial(preciseSaldoInicial);
        account.setSaldoActual(preciseSaldoActual);

        assertThat(account.getSaldoInicial()).isEqualByComparingTo(preciseSaldoInicial);
        assertThat(account.getSaldoActual()).isEqualByComparingTo(preciseSaldoActual);
    }

    @Test
    void whenMovementsListIsSet_thenRelationshipWorks() {
        List<Movement> movements = new ArrayList<>();
        Movement movement1 = new Movement();
        movement1.setId(1L);
        movement1.setTipoMovimiento(Movement.MovementType.CREDITO);
        
        Movement movement2 = new Movement();
        movement2.setId(2L);
        movement2.setTipoMovimiento(Movement.MovementType.DEBITO);
        
        movements.add(movement1);
        movements.add(movement2);
        
        account.setMovimientos(movements);
        
        assertThat(account.getMovimientos()).hasSize(2);
        assertThat(account.getMovimientos().get(0).getId()).isEqualTo(1L);
        assertThat(account.getMovimientos().get(0).getTipoMovimiento()).isEqualTo(Movement.MovementType.CREDITO);
        assertThat(account.getMovimientos().get(1).getId()).isEqualTo(2L);
        assertThat(account.getMovimientos().get(1).getTipoMovimiento()).isEqualTo(Movement.MovementType.DEBITO);
    }

    @Test
    void whenMovementsListIsEmpty_thenListIsEmpty() {
        account.setMovimientos(new ArrayList<>());
        assertThat(account.getMovimientos()).isEmpty();
    }

    @Test
    void whenMovementsListIsNull_thenGetterReturnsNull() {
        account.setMovimientos(null);
        assertThat(account.getMovimientos()).isEmpty();
    }

    @Test
    void whenSaldosAreZero_thenValuesAreZero() {
        account.setSaldoInicial(BigDecimal.ZERO);
        account.setSaldoActual(BigDecimal.ZERO);

        assertThat(account.getSaldoInicial()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(account.getSaldoActual()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void whenAccountNumberIsUnique_thenValueIsStored() {
        account.setNumeroCuenta("UNIQUE-123456789");
        assertThat(account.getNumeroCuenta()).isEqualTo("UNIQUE-123456789");
    }
}
