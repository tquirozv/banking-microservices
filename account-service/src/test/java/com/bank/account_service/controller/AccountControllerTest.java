package com.bank.account_service.controller;

import com.bank.account_service.dto.AccountCreateDto;
import com.bank.account_service.dto.AccountResponseDto;
import com.bank.account_service.dto.AccountUpdateDto;
import com.bank.account_service.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountResponseDto mockAccountResponse;
    private AccountCreateDto mockAccountCreateDto;
    private AccountUpdateDto mockAccountUpdateDto;

    @BeforeEach
    void setUp() {
        // Initialize test data
        mockAccountResponse = new AccountResponseDto();
        mockAccountCreateDto = new AccountCreateDto();
        mockAccountUpdateDto = new AccountUpdateDto();
    }

    @Test
    void createAccount_ShouldReturnCreatedStatus() {
        // Arrange
        when(accountService.createAccount(any(AccountCreateDto.class)))
                .thenReturn(mockAccountResponse);

        // Act
        ResponseEntity<AccountResponseDto> response = accountController.createAccount(mockAccountCreateDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockAccountResponse, response.getBody());
        verify(accountService).createAccount(mockAccountCreateDto);
    }

    @Test
    void getAccountById_ShouldReturnAccount() {
        // Arrange
        String numeroCuenta = "1L";
        when(accountService.getAccountByNumber(numeroCuenta)).thenReturn(mockAccountResponse);

        // Act
        ResponseEntity<AccountResponseDto> response = accountController.getAccountByNumber(numeroCuenta);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccountResponse, response.getBody());
        verify(accountService).getAccountByNumber(numeroCuenta);
    }

    @Test
    void getAccountByNumber_ShouldReturnAccount() {
        // Arrange
        String accountNumber = "123456";
        when(accountService.getAccountByNumber(accountNumber)).thenReturn(mockAccountResponse);

        // Act
        ResponseEntity<AccountResponseDto> response = accountController.getAccountByNumber(accountNumber);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccountResponse, response.getBody());
        verify(accountService).getAccountByNumber(accountNumber);
    }

    @Test
    void getAccountsByClientId_ShouldReturnAccountsList() {
        // Arrange
        Long clientId = 1L;
        List<AccountResponseDto> accounts = Collections.singletonList(mockAccountResponse);
        when(accountService.getAccountsByClientId(clientId)).thenReturn(accounts);

        // Act
        ResponseEntity<List<AccountResponseDto>> response = accountController.getAccountsByClientId(clientId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
        verify(accountService).getAccountsByClientId(clientId);
    }

    @Test
    void getAllAccounts_WithoutStatus_ShouldReturnAllAccounts() {
        // Arrange
        List<AccountResponseDto> accounts = Collections.singletonList(mockAccountResponse);
        when(accountService.getAllAccounts()).thenReturn(accounts);

        // Act
        ResponseEntity<List<AccountResponseDto>> response = accountController.getAllAccounts(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
        verify(accountService).getAllAccounts();
    }

    @Test
    void getAllAccounts_WithStatus_ShouldReturnFilteredAccounts() {
        // Arrange
        Boolean status = true;
        List<AccountResponseDto> accounts = Collections.singletonList(mockAccountResponse);
        when(accountService.getAccountsByStatus(status)).thenReturn(accounts);

        // Act
        ResponseEntity<List<AccountResponseDto>> response = accountController.getAllAccounts(status);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
        verify(accountService).getAccountsByStatus(status);
    }

    @Test
    void updateAccountStatus_ShouldReturnUpdatedAccount() {
        // Arrange
        String numeroCuenta = "1L";
        when(accountService.updateAccountStatus(eq(numeroCuenta), any(AccountUpdateDto.class)))
                .thenReturn(mockAccountResponse);

        // Act
        ResponseEntity<AccountResponseDto> response = accountController.updateAccountStatus(numeroCuenta, mockAccountUpdateDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccountResponse, response.getBody());
        verify(accountService).updateAccountStatus(numeroCuenta, mockAccountUpdateDto);
    }

    @Test
    void deleteAccount_ShouldCallServiceMethod() {
        // Arrange
        Long id = 1L;

        // Act
        accountController.deleteAccount(id);

        // Assert
        verify(accountService).deleteAccount(id);
    }
}
