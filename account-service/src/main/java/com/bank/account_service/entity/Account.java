package com.bank.account_service.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cuentas")
@Data
public class Account {
    @Id
    @Column(name = "numero_cuenta", unique = true, nullable = false)
    private String numeroCuenta;

    @Column(name = "tipo_cuenta", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType tipoCuenta;

    @Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial = BigDecimal.ZERO;

    @Column(name = "saldo_actual", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoActual = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean estado = true;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "numero_cuenta")
    private List<Movement> movimientos = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum AccountType {
        AHORRO, CORRIENTE
    }

    public Account() {
    }

    public Account(String numeroCuenta, AccountType tipoCuenta, BigDecimal saldoInicial,
                   BigDecimal saldoActual, Boolean estado, Long clienteId, List<Movement> movimientos,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoActual = saldoActual;
        this.estado = estado;
        this.clienteId = clienteId;
        this.movimientos = movimientos == null ? null : new ArrayList<>(movimientos);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Account(Account cuenta) {
        if (cuenta == null) {
            return;
        }
        this.numeroCuenta = cuenta.numeroCuenta;
        this.tipoCuenta = cuenta.tipoCuenta;
        this.saldoInicial = cuenta.saldoInicial;
        this.saldoActual = cuenta.saldoActual;
        this.estado =cuenta. estado;
        this.clienteId = cuenta.clienteId;
        this.movimientos = cuenta.movimientos == null ? null : new ArrayList<>(cuenta.movimientos);
        this.createdAt = cuenta.createdAt;
        this.updatedAt = cuenta.updatedAt;
    }

    public List<Movement> getMovimientos() {
        return new ArrayList<>(movimientos);
    }

    public void setMovimientos(List<Movement> movimientos) {
        this.movimientos = movimientos == null ? new ArrayList<>() : new ArrayList<>(movimientos);
    }
}