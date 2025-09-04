package com.bank.account_service.controller;

import com.bank.account_service.dto.MovementCreateDto;
import com.bank.account_service.dto.MovementResponseDto;
import com.bank.account_service.entity.Movement;
import com.bank.account_service.service.MovementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MovementControllerTest {

    @Mock
    private MovementService movementService;

    @InjectMocks
    private MovementController movementController;

    private MovementResponseDto mockMovementResponse;
    private MovementCreateDto mockMovementCreateDto;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        // Initialize test data
        mockMovementResponse = new MovementResponseDto();
        mockMovementCreateDto = new MovementCreateDto();
        startDate = LocalDateTime.now().minusDays(7);
        endDate = LocalDateTime.now();
    }

    @Test
    void createMovement_ShouldReturnCreatedStatus() {
        // Arrange
        when(movementService.createMovement(any(MovementCreateDto.class)))
                .thenReturn(mockMovementResponse);

        // Act
        ResponseEntity<MovementResponseDto> response = movementController.createMovement(mockMovementCreateDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockMovementResponse, response.getBody());
        verify(movementService).createMovement(mockMovementCreateDto);
    }

    @Test
    void getMovementById_ShouldReturnMovement() {
        // Arrange
        Long id = 1L;
        when(movementService.getMovementById(id)).thenReturn(mockMovementResponse);

        // Act
        ResponseEntity<MovementResponseDto> response = movementController.getMovementById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMovementResponse, response.getBody());
        verify(movementService).getMovementById(id);
    }

    @Test
    void getMovementsByAccountId_ShouldReturnMovementsList() {
        // Arrange
        Long accountId = 1L;
        List<MovementResponseDto> movements = Collections.singletonList(mockMovementResponse);
        when(movementService.getMovementsByAccountId(accountId)).thenReturn(movements);

        // Act
        ResponseEntity<List<MovementResponseDto>> response = movementController.getMovementsByAccountId(accountId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movements, response.getBody());
        verify(movementService).getMovementsByAccountId(accountId);
    }

    @Test
    void getMovementsByAccountIdAndDateRange_ShouldReturnMovementsList() {
        // Arrange
        Long accountId = 1L;
        List<MovementResponseDto> movements = Collections.singletonList(mockMovementResponse);
        when(movementService.getMovementsByAccountIdAndDateRange(accountId, startDate, endDate))
                .thenReturn(movements);

        // Act
        ResponseEntity<List<MovementResponseDto>> response = 
            movementController.getMovementsByAccountIdAndDateRange(accountId, startDate, endDate);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movements, response.getBody());
        verify(movementService).getMovementsByAccountIdAndDateRange(accountId, startDate, endDate);
    }

    @Test
    void getMovementsByAccountIdAndType_ShouldReturnMovementsList() {
        // Arrange
        Long accountId = 1L;
        Movement.MovementType movementType = Movement.MovementType.CREDITO;
        List<MovementResponseDto> movements = Collections.singletonList(mockMovementResponse);
        when(movementService.getMovementsByAccountIdAndType(accountId, movementType))
                .thenReturn(movements);

        // Act
        ResponseEntity<List<MovementResponseDto>> response = 
            movementController.getMovementsByAccountIdAndType(accountId, movementType);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movements, response.getBody());
        verify(movementService).getMovementsByAccountIdAndType(accountId, movementType);
    }

    @Test
    void deleteMovement_ShouldCallServiceMethod() {
        // Arrange
        Long id = 1L;

        // Act
        movementController.deleteMovement(id);

        // Assert
        verify(movementService).deleteMovement(id);
    }
}
