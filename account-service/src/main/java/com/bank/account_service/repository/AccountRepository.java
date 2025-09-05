package com.bank.account_service.repository;

import com.bank.account_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNumeroCuenta(String numeroCuenta);
    List<Account> findByClienteId(Long clienteId);
    List<Account> findByEstado(Boolean estado);
    boolean existsByNumeroCuenta(String accountId);
    @Query("SELECT DISTINCT a FROM Account a " +
            "LEFT JOIN FETCH a.movimientos m " +
            "WHERE a.clienteId = :clienteId " +
            "AND (m IS NOT NULL OR m.fecha BETWEEN :startDate AND :endDate) " +
            "ORDER BY a.numeroCuenta, m.fecha")
    List<Account> findByClienteIdAndFechaBetween(
            @Param("clienteId") Long clienteId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
