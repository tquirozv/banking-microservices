package com.bank.account_service.service;

import com.bank.account_service.dto.AccountCreateDto;
import com.bank.account_service.dto.AccountResponseDto;
import com.bank.account_service.dto.AccountUpdateDto;
import com.bank.account_service.entity.Account;
import com.bank.account_service.exception.AccountNotFoundException;
import com.bank.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountResponseDto createAccount(AccountCreateDto accountDto) {
        // Check if account number already exists
        accountRepository.findByNumeroCuenta(accountDto.getNumeroCuenta())
                .ifPresent(account -> {
                    throw new IllegalArgumentException("Account with number " + accountDto.getNumeroCuenta() + " already exists");
                });

        Account account = new Account();
        account.setNumeroCuenta(accountDto.getNumeroCuenta());
        account.setTipoCuenta(accountDto.getTipoCuenta());
        account.setSaldoInicial(accountDto.getSaldoInicial());
        account.setSaldoActual(accountDto.getSaldoInicial());
        account.setClienteId(accountDto.getClienteId());
        account.setEstado(true);

        return convertToDto(accountRepository.save(account));
    }

    @Override
    public AccountResponseDto getAccountById(Long id) {
        return accountRepository.findById(id)
            .map(this::convertToDto)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
    }

    @Override
    public AccountResponseDto getAccountByNumber(String accountNumber) {
        return accountRepository.findByNumeroCuenta(accountNumber)
            .map(this::convertToDto)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
    }

    @Override
    public List<AccountResponseDto> getAccountsByClientId(Long clientId) {
        return accountRepository.findByClienteId(clientId).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponseDto> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponseDto> getAccountsByStatus(Boolean estado) {
        return accountRepository.findByEstado(estado).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountResponseDto updateAccountStatus(Long id, AccountUpdateDto updateDto) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        
        account.setEstado(updateDto.getEstado());
        return convertToDto(accountRepository.save(account));
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        
        //delete the account with existing movements
        accountRepository.delete(account);
    }

    private AccountResponseDto convertToDto(Account account) {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setNumeroCuenta(account.getNumeroCuenta());
        dto.setTipoCuenta(account.getTipoCuenta());
        dto.setSaldoInicial(account.getSaldoInicial());
        dto.setSaldoActual(account.getSaldoActual());
        dto.setEstado(account.getEstado());
        dto.setClienteId(account.getClienteId());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        return dto;
    }
}
