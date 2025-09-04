package com.bank.account_service.service;

import com.bank.account_service.dto.MovementCreateDto;
import com.bank.account_service.dto.MovementResponseDto;
import com.bank.account_service.entity.Movement.MovementType;

import java.time.LocalDateTime;
import java.util.List;

public interface MovementService {
    MovementResponseDto createMovement(MovementCreateDto movementDto);
    
    MovementResponseDto getMovementById(Long id);
    
    List<MovementResponseDto> getMovementsByAccountId(Long accountId);
    
    List<MovementResponseDto> getMovementsByAccountIdAndDateRange(
        Long accountId, 
        LocalDateTime startDate, 
        LocalDateTime endDate
    );
    
    List<MovementResponseDto> getMovementsByAccountIdAndType(
        Long accountId, 
        MovementType movementType
    );
    
    void deleteMovement(Long id);
}
