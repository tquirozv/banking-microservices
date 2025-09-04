package com.bank.account_service.dto;

import com.bank.account_service.entity.Movement.MovementType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovementResponseDto {
    private Long id;
    private Long cuentaId;
    private LocalDateTime fecha;
    private MovementType tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private String descripcion;
    private LocalDateTime createdAt;
}
