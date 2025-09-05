package com.bank.account_service.service;

import com.bank.account_service.dto.AccountCreateDto;
import com.bank.account_service.dto.AccountResponseDto;
import com.bank.account_service.dto.AccountUpdateDto;
import com.bank.account_service.dto.AccountWithMovementsDto;
import com.bank.account_service.entity.Account;
import com.bank.account_service.exception.AccountNotFoundException;
import com.bank.account_service.repository.AccountRepository;
import com.bank.account_service.utils.ToDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        return ToDto.accountConvertToDto(accountRepository.save(account));
    }

    @Override
    public AccountResponseDto getAccountByNumber(String accountNumber) {
        return accountRepository.findByNumeroCuenta(accountNumber)
            .map(ToDto::accountConvertToDto)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
    }

    @Override
    public List<AccountResponseDto> getAccountsByClientId(Long clientId) {
        return accountRepository.findByClienteId(clientId).stream()
            .map(ToDto::accountConvertToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponseDto> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(ToDto::accountConvertToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponseDto> getAccountsByStatus(Boolean estado) {
        return accountRepository.findByEstado(estado).stream()
            .map(ToDto::accountConvertToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountResponseDto updateAccountStatus(String numeroCuenta, AccountUpdateDto updateDto) {
        Account account = accountRepository.findByNumeroCuenta(numeroCuenta)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + numeroCuenta));
        
        account.setEstado(updateDto.getEstado());
        return ToDto.accountConvertToDto(accountRepository.save(account));
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        
        //delete the account with existing movements
        accountRepository.delete(account);
    }

    @Override
    public List<AccountWithMovementsDto> getAccountsByClientIdAndDateRange(Long clientId, LocalDateTime startDate, 
                                                                           LocalDateTime endDate) {
        return accountRepository.findByClienteIdAndFechaBetween(clientId, startDate, endDate).stream()
                .map(ToDto::accountWithMovementsConvertToDto)
                .collect(Collectors.toList());
    }
    
}
