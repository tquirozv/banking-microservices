package com.bank.account_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountUpdateDto {

    @NotNull(message = "Status is required")
    private Boolean estado;
}
