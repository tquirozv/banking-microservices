package com.bank.account_service.service;

import com.bank.account_service.dto.*;
import java.util.List;

public interface AccountService {
    AccountResponseDto createAccount(AccountCreateDto accountDto);
    
    AccountResponseDto getAccountById(Long id);
    
    AccountResponseDto getAccountByNumber(String accountNumber);
    
    List<AccountResponseDto> getAccountsByClientId(Long clientId);
    
    List<AccountResponseDto> getAllAccounts();
    
    List<AccountResponseDto> getAccountsByStatus(Boolean estado);
    
    AccountResponseDto updateAccountStatus(Long id, AccountUpdateDto updateDto);
    
    void deleteAccount(Long id);

}
