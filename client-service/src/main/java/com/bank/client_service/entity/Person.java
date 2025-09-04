package com.bank.client_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "personas")
@Data
public class Person {

    // identificacion is the primary key
    @Id
    @Column(length = 20, nullable = false)
    private String identificacion;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(length = 10)
    private String genero;

    private Integer edad;

    @Column(length = 500)
    private String direccion;

    @Column(length = 15)
    private String telefono;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Person() {
    }

    public Person(String identificacion, String nombre, String genero, Integer edad, String direccion, String telefono,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.direccion = direccion;
        this.telefono = telefono;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Person(Person other) {
        if (other == null) {
            return;
        }
        this.identificacion = other.identificacion;
        this.nombre = other.nombre;
        this.genero = other.genero;
        this.edad = other.edad;
        this.direccion = other.direccion;
        this.telefono = other.telefono;
        this.createdAt = other.createdAt;
        this.updatedAt = other.updatedAt;
    }

}