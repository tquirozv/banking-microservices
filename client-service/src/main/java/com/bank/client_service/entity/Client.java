package com.bank.client_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
public class    Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clienteId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "persona_id", referencedColumnName = "identificacion", nullable = false)
    private Person persona;

    @Column(nullable = false, length = 255)
    private String contrasena;

    @Column(nullable = false)
    private Boolean estado = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Client() {
    }

    public Client(Long clienteId, Person persona, String contrasena, Boolean estado, LocalDateTime createdAt,
                  LocalDateTime updatedAt) {
        this.clienteId = clienteId;
        this.persona = (persona == null) ? null : new Person(persona);
        this.contrasena = contrasena;
        this.estado = estado;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Person getPersona() {
        return (this.persona == null) ? null : new Person(this.persona);
    }

    public void setPersona(Person persona) {
        this.persona = (persona == null) ? null : new Person(persona);
    }

}