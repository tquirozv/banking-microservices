package com.bank.account_service.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MovementTest {

    private Movement movement;
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setNumeroCuenta("123456");
        account.setTipoCuenta(Account.AccountType.AHORRO);
        
        movement = new Movement();
    }

    @Test
    void whenCreatingMovementWithDefaultConstructor_thenDefaultValuesAreSet() {
        Movement newMovement = new Movement();
        
        assertThat(newMovement.getId()).isNull();
        assertThat(newMovement.getCuentaId()).isNull();
        assertThat(newMovement.getFecha()).isNotNull(); // Default value is LocalDateTime.now()
        assertThat(newMovement.getTipoMovimiento()).isNull();
        assertThat(newMovement.getValor()).isNull();
        assertThat(newMovement.getSaldo()).isNull();
        assertThat(newMovement.getDescripcion()).isNull();
        assertThat(newMovement.getCreatedAt()).isNull(); // Will be set by @CreationTimestamp
    }

    @Test
    void whenCreatingMovementWithAllArgsConstructor_thenAllFieldsAreSet() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        
        Movement movement = new Movement(
            1L,
            account.getId(),
            now,
            Movement.MovementType.CREDITO,
            BigDecimal.valueOf(100.00),
            BigDecimal.valueOf(1100.00),
            "Deposit",
            createdAt
        );

        assertThat(movement.getId()).isEqualTo(1L);
        assertThat(movement.getCuentaId()).isEqualTo(account.getId());
        assertThat(movement.getFecha()).isEqualTo(now);
        assertThat(movement.getTipoMovimiento()).isEqualTo(Movement.MovementType.CREDITO);
        assertThat(movement.getValor()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(movement.getSaldo()).isEqualByComparingTo(BigDecimal.valueOf(1100.00));
        assertThat(movement.getDescripcion()).isEqualTo("Deposit");
        assertThat(movement.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void whenSettingAllFields_thenGettersReturnCorrectValues() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);

        movement.setId(1L);
        movement.setCuentaId(account.getId());
        movement.setFecha(now);
        movement.setTipoMovimiento(Movement.MovementType.CREDITO);
        movement.setValor(BigDecimal.valueOf(100.00));
        movement.setSaldo(BigDecimal.valueOf(1100.00));
        movement.setDescripcion("Deposit");
        movement.setCreatedAt(createdAt);

        assertThat(movement.getId()).isEqualTo(1L);
        assertThat(movement.getCuentaId()).isEqualTo(account.getId());
        assertThat(movement.getFecha()).isEqualTo(now);
        assertThat(movement.getTipoMovimiento()).isEqualTo(Movement.MovementType.CREDITO);
        assertThat(movement.getValor()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(movement.getSaldo()).isEqualByComparingTo(BigDecimal.valueOf(1100.00));
        assertThat(movement.getDescripcion()).isEqualTo("Deposit");
        assertThat(movement.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void testMovementTypeEnum() {
        assertThat(Movement.MovementType.CREDITO).isNotNull();
        assertThat(Movement.MovementType.DEBITO).isNotNull();
        assertThat(Movement.MovementType.values()).hasSize(2);
        assertThat(Movement.MovementType.valueOf("CREDITO")).isEqualTo(Movement.MovementType.CREDITO);
        assertThat(Movement.MovementType.valueOf("DEBITO")).isEqualTo(Movement.MovementType.DEBITO);
    }

    @Test
    void whenCreatingCreditoMovement_thenTypeIsCorrect() {
        movement.setTipoMovimiento(Movement.MovementType.CREDITO);
        movement.setValor(BigDecimal.valueOf(100.00));
        movement.setSaldo(BigDecimal.valueOf(1100.00));
        movement.setDescripcion("Credit transaction");

        assertThat(movement.getTipoMovimiento()).isEqualTo(Movement.MovementType.CREDITO);
        assertThat(movement.getValor()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(movement.getSaldo()).isEqualByComparingTo(BigDecimal.valueOf(1100.00));
        assertThat(movement.getDescripcion()).isEqualTo("Credit transaction");
    }

    @Test
    void whenCreatingDebitoMovement_thenTypeIsCorrect() {
        movement.setTipoMovimiento(Movement.MovementType.DEBITO);
        movement.setValor(BigDecimal.valueOf(50.00));
        movement.setSaldo(BigDecimal.valueOf(950.00));
        movement.setDescripcion("Debit transaction");

        assertThat(movement.getTipoMovimiento()).isEqualTo(Movement.MovementType.DEBITO);
        assertThat(movement.getValor()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
        assertThat(movement.getSaldo()).isEqualByComparingTo(BigDecimal.valueOf(950.00));
        assertThat(movement.getDescripcion()).isEqualTo("Debit transaction");
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        
        Movement movement1 = new Movement();
        movement1.setId(1L);
        movement1.setCuentaId(account.getId());
        movement1.setFecha(now);
        movement1.setTipoMovimiento(Movement.MovementType.CREDITO);

        Movement movement2 = new Movement();
        movement2.setId(1L);
        movement2.setCuentaId(account.getId());
        movement2.setFecha(now);
        movement2.setTipoMovimiento(Movement.MovementType.CREDITO);

        Movement movement3 = new Movement();
        movement3.setId(2L);
        movement3.setTipoMovimiento(Movement.MovementType.DEBITO);

        assertThat(movement1).isEqualTo(movement2);
        assertThat(movement1).isNotEqualTo(movement3);
        assertThat(movement1.hashCode()).isEqualTo(movement2.hashCode());
    }

    @Test
    void testToString() {
        movement.setId(1L);
        movement.setCuentaId(account.getId());
        movement.setTipoMovimiento(Movement.MovementType.CREDITO);
        movement.setValor(BigDecimal.valueOf(100.00));

        String toString = movement.toString();
        
        assertThat(toString).contains("Movement");
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("tipoMovimiento=CREDITO");
        assertThat(toString).contains("valor=100");
    }

    @Test
    void whenDescripcionIsNull_thenGetterReturnsNull() {
        movement.setDescripcion(null);
        assertThat(movement.getDescripcion()).isNull();
    }

    @Test
    void whenDescripcionIsEmpty_thenGetterReturnsEmpty() {
        movement.setDescripcion("");
        assertThat(movement.getDescripcion()).isEmpty();
    }

    @Test
    void whenDecimalValuesHavePrecision_thenPrecisionIsPreserved() {
        BigDecimal preciseValue = new BigDecimal("123.456789");
        BigDecimal preciseSaldo = new BigDecimal("987.654321");
        
        movement.setValor(preciseValue);
        movement.setSaldo(preciseSaldo);

        assertThat(movement.getValor()).isEqualByComparingTo(preciseValue);
        assertThat(movement.getSaldo()).isEqualByComparingTo(preciseSaldo);
    }

    @Test
    void whenAccountRelationshipIsSet_thenRelationshipWorks() {
        Account testAccount = new Account();
        testAccount.setId(123L);
        testAccount.setNumeroCuenta("987654");
        
        movement.setCuentaId(testAccount.getId());
        
        assertThat(movement.getCuentaId()).isNotNull();
        assertThat(movement.getCuentaId()).isEqualTo(123L);
    }
}
