package com.bank.account_service.repository;

import com.bank.account_service.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByCuentaId(Long accountId);
    List<Movement> findByCuentaIdAndFechaBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    List<Movement> findByCuentaIdAndTipoMovimiento(Long accountId, Movement.MovementType movementType);
}
