package com.bank.client_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientUpdateDto {
    private Long clienteid;

    @NotNull(message = "Person data is required")
    @Valid
    private PersonDto persona;

    @Size(max = 255, message = "Password must be at most 255 characters")
    private String contrasena;

    @NotNull(message = "Status is required")
    private Boolean estado;
}
