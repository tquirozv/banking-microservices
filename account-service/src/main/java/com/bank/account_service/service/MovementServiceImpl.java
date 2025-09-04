package com.bank.account_service.service;

import com.bank.account_service.dto.MovementCreateDto;
import com.bank.account_service.dto.MovementResponseDto;
import com.bank.account_service.entity.Account;
import com.bank.account_service.entity.Movement;
import com.bank.account_service.exception.AccountNotFoundException;
import com.bank.account_service.exception.MovementNotFoundException;
import com.bank.account_service.repository.AccountRepository;
import com.bank.account_service.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public MovementResponseDto createMovement(MovementCreateDto movementDto) {
        Account account = accountRepository.findByNumeroCuenta(movementDto.getCuentaId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + movementDto.getCuentaId()));

        // Validate account is active
        if (!account.getEstado()) {
            throw new RuntimeException("Cannot create movement for inactive account");
        }

        // Validate movement amount is not zero
        if (movementDto.getValor() == null || movementDto.getValor().compareTo(BigDecimal.ZERO) == 0) {
            throw new RuntimeException("Movement amount cannot be zero");
        }

        // Always store the absolute value of the amount
        BigDecimal absAmount = movementDto.getValor().abs();

        Movement movement = new Movement();
        movement.setNumeroCuenta(account.getNumeroCuenta());
        movement.setFecha(LocalDateTime.now());
        movement.setTipoMovimiento(movementDto.getTipoMovimiento());
        movement.setValor(absAmount);
        movement.setDescripcion(movementDto.getDescripcion());

        BigDecimal newBalance;
        if (movementDto.getTipoMovimiento() == Movement.MovementType.CREDITO) {
            newBalance = account.getSaldoActual().add(absAmount);
        } else {
            if (account.getSaldoActual().compareTo(absAmount) < 0) {
                throw new RuntimeException("Insufficient funds");
            }
            newBalance = account.getSaldoActual().subtract(absAmount);
        }

        account.setSaldoActual(newBalance);
        accountRepository.save(account);
        Movement savedMovement = movementRepository.save(movement);

        return convertToDto(savedMovement);
    }

    @Override
    public MovementResponseDto getMovementById(Long id) {
        return movementRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new MovementNotFoundException("Movement not found"));
    }

    @Override
    public List<MovementResponseDto> getMovementsByAccountId(String accountId) {
        return movementRepository.findByNumeroCuenta(accountId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovementResponseDto> getMovementsByAccountIdAndDateRange(
            String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return movementRepository.findByNumeroCuentaAndFechaBetween(accountId, startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovementResponseDto> getMovementsByAccountIdAndType(
            String accountId, Movement.MovementType movementType) {
        return movementRepository.findByNumeroCuentaAndTipoMovimiento(accountId, movementType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMovement(Long id) {
        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> new MovementNotFoundException("Movement not found"));

        Account account = accountRepository.findByNumeroCuenta(movement.getNumeroCuenta())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + movement.getNumeroCuenta()));

        // Reverse the movement effect on account balance
        if (movement.getTipoMovimiento() == Movement.MovementType.CREDITO) {
            account.setSaldoActual(account.getSaldoActual().subtract(movement.getValor()));
        } else {
            account.setSaldoActual(account.getSaldoActual().add(movement.getValor()));
        }

        accountRepository.save(account);
        movementRepository.delete(movement);
    }

    private MovementResponseDto convertToDto(Movement movement) {
        MovementResponseDto dto = new MovementResponseDto();
        dto.setId(movement.getId());
        dto.setCuentaId(movement.getNumeroCuenta());
        dto.setFecha(movement.getFecha());
        dto.setTipoMovimiento(movement.getTipoMovimiento());
        dto.setValor(movement.getValor());
        dto.setSaldo(movement.getSaldo());
        dto.setDescripcion(movement.getDescripcion());
        dto.setCreatedAt(movement.getCreatedAt());
        return dto;
    }
}