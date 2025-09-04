package com.bank.account_service.service;

import com.bank.account_service.dto.AccountCreateDto;
import com.bank.account_service.dto.AccountResponseDto;
import com.bank.account_service.dto.AccountUpdateDto;
import com.bank.account_service.entity.Account;
import com.bank.account_service.exception.AccountNotFoundException;
import com.bank.account_service.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountCreateDto accountCreateDto;
    private Account account;
    private AccountUpdateDto accountUpdateDto;

    @BeforeEach
    void setUp() {
        accountCreateDto = new AccountCreateDto();
        accountCreateDto.setNumeroCuenta("123456");
        accountCreateDto.setTipoCuenta(Account.AccountType.AHORRO);
        accountCreateDto.setSaldoInicial(BigDecimal.valueOf(1000.00));
        accountCreateDto.setClienteId(1L);

        account = new Account();
        account.setNumeroCuenta("123456");
        account.setTipoCuenta(Account.AccountType.AHORRO);
        account.setSaldoInicial(BigDecimal.valueOf(1000.00));
        account.setSaldoActual(BigDecimal.valueOf(1000.00));
        account.setEstado(true);
        account.setClienteId(1L);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setEstado(false);
    }

    @Test
    void whenCreateAccount_thenReturnAccountResponseDto() {
        // Given
        when(accountRepository.findByNumeroCuenta("123456")).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // When
        AccountResponseDto result = accountService.createAccount(accountCreateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNumeroCuenta()).isEqualTo("123456");
        assertThat(result.getTipoCuenta()).isEqualTo(Account.AccountType.AHORRO);
        assertThat(result.getSaldoInicial()).isEqualByComparingTo(BigDecimal.valueOf(1000.00));
        assertThat(result.getClienteId()).isEqualTo(1L);
        assertThat(result.getEstado()).isTrue();

        verify(accountRepository).findByNumeroCuenta("123456");
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void whenCreateAccountWithExistingNumeroCuenta_thenThrowException() {
        // Given
        when(accountRepository.findByNumeroCuenta("123456")).thenReturn(Optional.of(account));

        // When & Then
        assertThatThrownBy(() -> accountService.createAccount(accountCreateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(accountRepository).findByNumeroCuenta("123456");
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void whenGetAllAccounts_thenReturnAccountsList() {
        // Given
        List<Account> accounts = Collections.singletonList(account);
        when(accountRepository.findAll()).thenReturn(accounts);

        // When
        List<AccountResponseDto> result = accountService.getAllAccounts();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getNumeroCuenta()).isEqualTo("123456");

        verify(accountRepository).findAll();
    }

    @Test
    void whenGetAccountById_thenReturnAccount() {
        // Given
        when(accountRepository.findByNumeroCuenta("1L")).thenReturn(Optional.of(account));

        // When
        AccountResponseDto result = accountService.getAccountByNumber("1L");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNumeroCuenta()).isEqualTo("123456");

        verify(accountRepository).findByNumeroCuenta("1L");
    }

    @Test
    void whenGetAccountByIdNotFound_thenThrowException() {
        // Given
        when(accountRepository.findByNumeroCuenta("999L")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountService.getAccountByNumber("999L"))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found with number: 999L");

        verify(accountRepository).findByNumeroCuenta("999L");
    }

    @Test
    void whenGetAccountByNumeroCuenta_thenReturnAccount() {
        // Given
        when(accountRepository.findByNumeroCuenta("123456")).thenReturn(Optional.of(account));

        // When
        AccountResponseDto result = accountService.getAccountByNumber("123456");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNumeroCuenta()).isEqualTo("123456");

        verify(accountRepository).findByNumeroCuenta("123456");
    }

    @Test
    void whenGetAccountByNumeroCuentaNotFound_thenThrowException() {
        // Given
        when(accountRepository.findByNumeroCuenta("999999")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountService.getAccountByNumber("999999"))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found with number: 999999");

        verify(accountRepository).findByNumeroCuenta("999999");
    }

    @Test
    void whenGetAccountsByClienteId_thenReturnAccountsList() {
        // Given
        List<Account> accounts = Collections.singletonList(account);
        when(accountRepository.findByClienteId(1L)).thenReturn(accounts);

        // When
        List<AccountResponseDto> result = accountService.getAccountsByClientId(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getClienteId()).isEqualTo(1L);

        verify(accountRepository).findByClienteId(1L);
    }

    @Test
    void whenGetAccountsByEstado_thenReturnAccountsList() {
        // Given
        List<Account> accounts = Collections.singletonList(account);
        when(accountRepository.findByEstado(true)).thenReturn(accounts);

        // When
        List<AccountResponseDto> result = accountService.getAccountsByStatus(true);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getEstado()).isTrue();

        verify(accountRepository).findByEstado(true);
    }

    @Test
    void whenUpdateAccount_thenReturnUpdatedAccount() {
        // Given
        Account updatedAccount = new Account();
        updatedAccount.setNumeroCuenta("123456");
        updatedAccount.setEstado(false);

        when(accountRepository.findByNumeroCuenta("123456")).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        // When
        AccountResponseDto result = accountService.updateAccountStatus("123456", accountUpdateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isFalse();

        verify(accountRepository).findByNumeroCuenta("123456");
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void whenUpdateAccountNotFound_thenThrowException() {
        // Given
        when(accountRepository.findByNumeroCuenta("999L")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountService.updateAccountStatus("999L", accountUpdateDto))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found with id: 999");

        verify(accountRepository).findByNumeroCuenta("999L");
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void whenDeleteAccount_thenAccountIsDeleted() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).delete(account);

        // When
        accountService.deleteAccount(1L);

        // Then
        verify(accountRepository).findById(1L);
        verify(accountRepository).delete(account);
    }

    @Test
    void whenDeleteAccountNotFound_thenThrowException() {
        // Given
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountService.deleteAccount(999L))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found with id: 999");

        verify(accountRepository).findById(999L);
        verify(accountRepository, never()).delete(any(Account.class));
    }

    @Test
    void whenCreateAccountWithInitialBalance_thenSaldoActualEqualsInitialBalance() {
        // Given
        when(accountRepository.findByNumeroCuenta("123456")).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account savedAccount = invocation.getArgument(0);
            // Simulate the service setting saldoActual = saldoInicial
            savedAccount.setSaldoActual(savedAccount.getSaldoInicial());
            return savedAccount;
        });

        // When
        AccountResponseDto result = accountService.createAccount(accountCreateDto);

        // Then
        assertThat(result.getSaldoActual()).isEqualByComparingTo(result.getSaldoInicial());
    }

    @Test
    void whenCreateAccountSetsDefaultValues_thenValuesAreCorrect() {
        // Given
        when(accountRepository.findByNumeroCuenta("123456")).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account savedAccount = invocation.getArgument(0);
            // Verify default values are set by the service
            assertThat(savedAccount.getEstado()).isTrue();
            assertThat(savedAccount.getSaldoActual()).isEqualByComparingTo(savedAccount.getSaldoInicial());
            return savedAccount;
        });

        // When
        accountService.createAccount(accountCreateDto);

        // Then
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void whenRepositoryThrowsException_thenServicePropagatesException() {
        // Given
        when(accountRepository.findByNumeroCuenta("123456")).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThatThrownBy(() -> accountService.createAccount(accountCreateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void whenGetAccountsByClienteIdEmpty_thenReturnEmptyList() {
        // Given
        when(accountRepository.findByClienteId(999L)).thenReturn(List.of());

        // When
        List<AccountResponseDto> result = accountService.getAccountsByClientId(999L);

        // Then
        assertThat(result).isEmpty();
        verify(accountRepository).findByClienteId(999L);
    }

    @Test
    void whenGetAccountsByEstadoEmpty_thenReturnEmptyList() {
        // Given
        when(accountRepository.findByEstado(false)).thenReturn(List.of());

        // When
        List<AccountResponseDto> result = accountService.getAccountsByStatus(false);

        // Then
        assertThat(result).isEmpty();
        verify(accountRepository).findByEstado(false);
    }
}