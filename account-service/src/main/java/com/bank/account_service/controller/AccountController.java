package com.bank.account_service.controller;

import com.bank.account_service.service.AccountService;
import jakarta.validation.Valid;
import com.bank.account_service.dto.AccountCreateDto;
import com.bank.account_service.dto.AccountResponseDto;
import com.bank.account_service.dto.AccountUpdateDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/cuentas")
public class AccountController {

    private final AccountService accountService;

    public AccountController(final AccountService accountService) {
        // Using final parameter and direct assignment for immutability
        this.accountService = Objects.requireNonNull(accountService, "AccountService must not be null");
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountCreateDto accountDto) {
        return new ResponseEntity<>(
            accountService.createAccount(accountDto), 
            HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccountByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(accountService.getAccountsByClientId(clientId));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts(@RequestParam(required = false) Boolean estado) {
        if (estado != null) {
            return ResponseEntity.ok(accountService.getAccountsByStatus(estado));
        }
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponseDto> updateAccountStatus(@PathVariable Long id,
                                                                  @Valid @RequestBody AccountUpdateDto updateDto) {
        return ResponseEntity.ok(accountService.updateAccountStatus(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }

}
