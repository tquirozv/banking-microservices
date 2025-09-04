package com.bank.account_service.service;

import com.bank.account_service.dto.MovementCreateDto;
import com.bank.account_service.dto.MovementResponseDto;
import com.bank.account_service.entity.Account;
import com.bank.account_service.entity.Movement;
import com.bank.account_service.repository.AccountRepository;
import com.bank.account_service.repository.MovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovementServiceImplTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private MovementServiceImpl movementService;

    private MovementCreateDto movementCreateDto;
    private Account account;
    private Movement movement;

    @BeforeEach
    void setUp() {
        movementCreateDto = new MovementCreateDto();
        movementCreateDto.setCuentaId(1L);
        movementCreateDto.setTipoMovimiento(Movement.MovementType.CREDITO);
        movementCreateDto.setValor(BigDecimal.valueOf(100.00));
        movementCreateDto.setDescripcion("Deposit");

        account = new Account();
        account.setId(1L);
        account.setNumeroCuenta("123456");
        account.setSaldoActual(BigDecimal.valueOf(1000.00));
        account.setEstado(true);

        movement = new Movement();
        movement.setId(1L);
        movement.setCuentaId(account.getId());
        movement.setFecha(LocalDateTime.now());
        movement.setTipoMovimiento(Movement.MovementType.CREDITO);
        movement.setValor(BigDecimal.valueOf(100.00));
        movement.setSaldo(BigDecimal.valueOf(1100.00));
        movement.setDescripcion("Deposit");
    }

    @Test
    void whenCreateCreditMovement_thenReturnMovementResponseDto() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        // When
        MovementResponseDto result = movementService.createMovement(movementCreateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTipoMovimiento()).isEqualTo(Movement.MovementType.CREDITO);
        assertThat(result.getValor()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(result.getCuentaId()).isEqualTo(1L);

        verify(accountRepository).findById(1L);
        verify(movementRepository).save(any(Movement.class));
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void whenCreateDebitMovement_thenReturnMovementResponseDto() {
        // Given
        movementCreateDto.setTipoMovimiento(Movement.MovementType.DEBITO);
        movementCreateDto.setValor(BigDecimal.valueOf(50.00));

        Movement debitMovement = new Movement();
        debitMovement.setId(2L);
        debitMovement.setCuentaId(account.getId());
        debitMovement.setTipoMovimiento(Movement.MovementType.DEBITO);
        debitMovement.setValor(BigDecimal.valueOf(50.00));
        debitMovement.setSaldo(BigDecimal.valueOf(950.00));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(movementRepository.save(any(Movement.class))).thenReturn(debitMovement);

        // When
        MovementResponseDto result = movementService.createMovement(movementCreateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTipoMovimiento()).isEqualTo(Movement.MovementType.DEBITO);
        assertThat(result.getValor()).isEqualByComparingTo(BigDecimal.valueOf(50.00));

        verify(accountRepository).findById(1L);
        verify(movementRepository).save(any(Movement.class));
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void whenCreateMovementWithInsufficientFunds_thenThrowException() {
        // Given
        movementCreateDto.setTipoMovimiento(Movement.MovementType.DEBITO);
        movementCreateDto.setValor(BigDecimal.valueOf(2000.00));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // When & Then
        assertThatThrownBy(() -> movementService.createMovement(movementCreateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("funds");

        verify(accountRepository).findById(1L);
        verify(movementRepository, never()).save(any(Movement.class));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void whenCreateMovementWithNonExistentAccount_thenThrowException() {
        // Given
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());
        movementCreateDto.setCuentaId(999L);

        // When & Then
        assertThatThrownBy(() -> movementService.createMovement(movementCreateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");

        verify(accountRepository).findById(999L);
        verify(movementRepository, never()).save(any(Movement.class));
    }

    @Test
    void whenGetMovementsByAccountId_thenReturnMovementsList() {
        // Given
        List<Movement> movements = Arrays.asList(movement);
        when(movementRepository.findByCuentaId(1L)).thenReturn(movements);

        // When
        List<MovementResponseDto> result = movementService.getMovementsByAccountId(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCuentaId()).isEqualTo(1L);

        verify(movementRepository).findByCuentaId(1L);
    }

    @Test
    void whenGetMovementsByAccountIdEmpty_thenReturnEmptyList() {
        // Given
        when(movementRepository.findByCuentaId(999L)).thenReturn(Arrays.asList());

        // When
        List<MovementResponseDto> result = movementService.getMovementsByAccountId(999L);

        // Then
        assertThat(result).isEmpty();
        verify(movementRepository).findByCuentaId(999L);
    }

    @Test
    void whenCreateMovementWithZeroAmount_thenThrowException() {
        // Given
        movementCreateDto.setValor(BigDecimal.ZERO);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // When & Then
        assertThatThrownBy(() -> movementService.createMovement(movementCreateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("amount");

        verify(movementRepository, never()).save(any(Movement.class));
    }

    @Test
    void whenCreateCreditoMovement_shouldHandlePositiveAmount() {
// Given
        BigDecimal initialBalance = BigDecimal.valueOf(1000.00);
        account.setSaldoActual(initialBalance);
        BigDecimal creditAmount = BigDecimal.valueOf(100.00);

        movementCreateDto.setValor(creditAmount);
        movementCreateDto.setTipoMovimiento(Movement.MovementType.CREDITO);

        Movement savedMovement = new Movement();
        savedMovement.setId(1L);
        savedMovement.setCuentaId(account.getId());
        savedMovement.setValor(creditAmount);
        savedMovement.setTipoMovimiento(Movement.MovementType.CREDITO);
        savedMovement.setSaldo(initialBalance.add(creditAmount));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(movementRepository.save(any(Movement.class))).thenReturn(savedMovement);

        // When
        MovementResponseDto result = movementService.createMovement(movementCreateDto);

        // Then
        verify(movementRepository).save(any(Movement.class));
        assertThat(result).isNotNull();
        assertThat(result.getValor()).isEqualByComparingTo(creditAmount);
        assertThat(result.getSaldo()).isEqualByComparingTo(initialBalance.add(creditAmount));

    }

    @Test
    void whenCreateDebitoMovement_shouldHandleNegativeAmount() {
// Given
        BigDecimal initialBalance = BigDecimal.valueOf(1000.00);
        account.setSaldoActual(initialBalance);
        BigDecimal debitAmount = BigDecimal.valueOf(-100.00);

        movementCreateDto.setValor(debitAmount);
        movementCreateDto.setTipoMovimiento(Movement.MovementType.DEBITO);

        Movement savedMovement = new Movement();
        savedMovement.setId(1L);
        savedMovement.setCuentaId(account.getId());
        savedMovement.setValor(debitAmount);
        savedMovement.setTipoMovimiento(Movement.MovementType.DEBITO);
        savedMovement.setSaldo(initialBalance.subtract(debitAmount.abs()));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(movementRepository.save(any(Movement.class))).thenReturn(savedMovement);

        // When
        MovementResponseDto result = movementService.createMovement(movementCreateDto);

        // Then
        verify(movementRepository).save(any(Movement.class));
        assertThat(result).isNotNull();
        assertThat(result.getValor()).isEqualByComparingTo(debitAmount);
        assertThat(result.getSaldo()).isEqualByComparingTo(initialBalance.subtract(debitAmount.abs()));

    }


    @Test
    void whenCreateMovementWithInactiveAccount_thenThrowException() {
        // Given
        account.setEstado(false);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // When & Then
        assertThatThrownBy(() -> movementService.createMovement(movementCreateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("active");

        verify(movementRepository, never()).save(any(Movement.class));
    }
}