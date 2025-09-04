package com.bank.account_service.dto;

import com.bank.account_service.entity.Movement.MovementType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MovementCreateDto {
    @NotNull(message = "Account ID is required")
    private String cuentaId;

    @NotNull(message = "Movement type is required")
    private MovementType tipoMovimiento;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal valor;

    private String descripcion;
}
