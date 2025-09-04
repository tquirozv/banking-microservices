package com.bank.account_service.dto;

import com.bank.account_service.entity.Account.AccountType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountResponseDto {
    private Long id;
    private String numeroCuenta;
    private AccountType tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    private Boolean estado;
    private Long clienteId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
