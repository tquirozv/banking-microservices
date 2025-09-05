package com.bank.account_service.dto;

import com.bank.account_service.entity.Movement.MovementType;
import com.bank.account_service.entity.Account.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Long clientId;
    private String nombre;
    private String identificacion;
    private String numeroCuenta;
    private AccountType tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    private LocalDateTime fecha;
    private MovementType tipoMovimiento;
    private BigDecimal valorMovimiento;
    private BigDecimal saldoMovimiento;
    private LocalDateTime createdAt;
}
