package com.bank.account_service.service;

import com.bank.account_service.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountService {
    AccountResponseDto createAccount(AccountCreateDto accountDto);
    
    AccountResponseDto getAccountByNumber(String accountNumber);
    
    List<AccountResponseDto> getAccountsByClientId(Long clientId);
    
    List<AccountResponseDto> getAllAccounts();
    
    List<AccountResponseDto> getAccountsByStatus(Boolean estado);
    
    AccountResponseDto updateAccountStatus(String numeroCuenta, AccountUpdateDto updateDto);
    
    void deleteAccount(Long id);

    List<AccountWithMovementsDto> getAccountsByClientIdAndDateRange(Long clientId,
                                                                    LocalDateTime startDate,
                                                                    LocalDateTime endDate);

}
