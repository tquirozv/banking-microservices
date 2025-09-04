package com.bank.account_service.dto;

import com.bank.account_service.entity.Account.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountCreateDto {
    @NotBlank(message = "Account number is required")
    private String numeroCuenta;

    @NotNull(message = "Account type is required")
    private AccountType tipoCuenta;

    @NotNull(message = "Initial balance is required")
    @PositiveOrZero(message = "Initial balance must be positive")
    private BigDecimal saldoInicial;

    @NotNull(message = "Client ID is required")
    private Long clienteId;
}
