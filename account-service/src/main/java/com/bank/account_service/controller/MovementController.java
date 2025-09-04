package com.bank.account_service.controller;

import com.bank.account_service.dto.MovementCreateDto;
import com.bank.account_service.dto.MovementResponseDto;
import com.bank.account_service.entity.Movement;
import com.bank.account_service.service.MovementService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/movimientos")
public class MovementController {

    private final MovementService movementService;


    public MovementController(MovementService movementService) {
        this.movementService = Objects.requireNonNull(movementService, "MovementService must not be null");
    }

    @PostMapping
    public ResponseEntity<MovementResponseDto> createMovement(@Valid @RequestBody MovementCreateDto movementDto) {
        return new ResponseEntity<>(
            movementService.createMovement(movementDto), 
            HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementResponseDto> getMovementById(@PathVariable Long id) {
        return ResponseEntity.ok(movementService.getMovementById(id));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<MovementResponseDto>> getMovementsByAccountId(@PathVariable String accountId) {
        return ResponseEntity.ok(movementService.getMovementsByAccountId(accountId));
    }

    @GetMapping("/account/{accountId}/date-range")
    public ResponseEntity<List<MovementResponseDto>> getMovementsByAccountIdAndDateRange(
            @PathVariable String accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(
            movementService.getMovementsByAccountIdAndDateRange(accountId, startDate, endDate)
        );
    }

    @GetMapping("/account/{accountId}/type/{movementType}")
    public ResponseEntity<List<MovementResponseDto>> getMovementsByAccountIdAndType(
            @PathVariable String accountId,
            @PathVariable Movement.MovementType movementType) {
        return ResponseEntity.ok(
            movementService.getMovementsByAccountIdAndType(accountId, movementType)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovement(@PathVariable Long id) {
        movementService.deleteMovement(id);
    }
}
