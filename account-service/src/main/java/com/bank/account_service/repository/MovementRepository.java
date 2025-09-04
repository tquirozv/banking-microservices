package com.bank.account_service.repository;

import com.bank.account_service.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByNumeroCuenta(String accountId);
    List<Movement> findByNumeroCuentaAndFechaBetween(String accountId, LocalDateTime startDate, LocalDateTime endDate);
    List<Movement> findByNumeroCuentaAndTipoMovimiento(String accountId, Movement.MovementType movementType);
}
