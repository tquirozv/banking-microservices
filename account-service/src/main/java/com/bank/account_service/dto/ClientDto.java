package com.bank.account_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientDto {
    private Long clienteId;

    @NotNull(message = "Person data is required")
    @Valid
    private PersonDto persona;

    @NotBlank(message = "Password is required")
    @Size(max = 255, message = "Password must be at most 255 characters")
    private String contrasena;

    @NotNull(message = "Status is required")
    private Boolean estado;

    public ClientDto() {
    }

    public ClientDto(Long clienteId, PersonDto persona, String contrasena, Boolean estado) {
        this.clienteId = clienteId;
        this.persona = (persona == null) ? null : new PersonDto(persona);
        this.contrasena = contrasena;
        this.estado = estado;
    }

    // defensive getter to avoid exposing internal representation
    public PersonDto getPersona() {
        return (this.persona == null) ? null : new PersonDto(this.persona);
    }

    // defensive setter to avoid storing external mutable object
    public void setPersona(PersonDto persona) {
        this.persona = (persona == null) ? null : new PersonDto(persona);
    }

}