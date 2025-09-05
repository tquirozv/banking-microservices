package com.bank.account_service.dto;

import com.bank.account_service.entity.Account.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountWithMovementsDto {
    private Long id;
    private String numeroCuenta;
    private AccountType tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    private Boolean estado;
    private Long clienteId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MovementResponseDto> movements;
}