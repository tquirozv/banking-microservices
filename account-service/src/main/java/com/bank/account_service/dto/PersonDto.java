package com.bank.account_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonDto {

    @NotBlank(message = "Identification is required")
    @Size(max = 20, message = "Identification must be at most 20 characters")
    private String identificacion;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String nombre;

    @Pattern(regexp = "^[MF]$", message = "Gender must be M or F")
    private String genero;

    @Min(value = 0, message = "Age must be positive")
    private Integer edad;

    @Size(max = 500, message = "Address must be at most 500 characters")
    private String direccion;

    @Pattern(regexp = "^[0-9-]{10,15}$", message = "Invalid phone format")
    private String telefono;

    // Required by Jackson/Spring MVC
    public PersonDto() {
    }

    public PersonDto(String identificacion, String nombre, String genero, Integer edad, String direccion,
                     String telefono) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public PersonDto(PersonDto other) {
        if (other == null) {
            return;
        }
        this.identificacion = other.identificacion;
        this.nombre = other.nombre;
        this.genero = other.genero;
        this.edad = other.edad;
        this.direccion = other.direccion;
        this.telefono = other.telefono;
    }


}