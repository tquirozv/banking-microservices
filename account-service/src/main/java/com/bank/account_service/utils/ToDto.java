package com.bank.account_service.utils;

import com.bank.account_service.dto.AccountResponseDto;
import com.bank.account_service.dto.AccountWithMovementsDto;
import com.bank.account_service.dto.ClientDto;
import com.bank.account_service.dto.MovementResponseDto;
import com.bank.account_service.dto.ReportDto;
import com.bank.account_service.entity.Account;
import com.bank.account_service.entity.Movement;

import java.util.List;

public class ToDto {

    public static MovementResponseDto movementConvertToDto(Movement movement) {
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

    public static AccountResponseDto accountConvertToDto(Account account) {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setNumeroCuenta(account.getNumeroCuenta());
        dto.setTipoCuenta(account.getTipoCuenta());
        dto.setSaldoInicial(account.getSaldoInicial());
        dto.setSaldoActual(account.getSaldoActual());
        dto.setEstado(account.getEstado());
        dto.setClienteId(account.getClienteId());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        return dto;
    }

    public static AccountWithMovementsDto accountWithMovementsConvertToDto(Account account) {
        AccountWithMovementsDto dto = new AccountWithMovementsDto();
        dto.setNumeroCuenta(account.getNumeroCuenta());
        dto.setTipoCuenta(account.getTipoCuenta());
        dto.setSaldoInicial(account.getSaldoInicial());
        dto.setSaldoActual(account.getSaldoActual());
        dto.setEstado(account.getEstado());
        dto.setClienteId(account.getClienteId());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        if (!account.getMovimientos().isEmpty()) {
            dto.setMovements(account.getMovimientos().stream().map(ToDto::movementConvertToDto).toList());
        }
        return dto;
    }

    public static List<ReportDto> reportCreation(ClientDto clientDto, List<AccountWithMovementsDto> accounts){
        return accounts.stream()
                .map(acc -> reportDto(clientDto, acc))
                .flatMap(List::stream)
                .toList();
    }

    public static List<ReportDto> reportDto(ClientDto clientDto, AccountWithMovementsDto account) {
        return account.getMovements().stream().map(movements -> {
            ReportDto dto = new ReportDto();
            dto.setClientId(clientDto.getClienteId());
            dto.setNombre(clientDto.getPersona().getNombre());
            dto.setIdentificacion(clientDto.getPersona().getIdentificacion());
            dto.setNumeroCuenta(account.getNumeroCuenta());
            dto.setTipoCuenta(account.getTipoCuenta());
            dto.setSaldoInicial(account.getSaldoInicial());
            dto.setSaldoActual(account.getSaldoActual());
            dto.setFecha(movements.getFecha());
            dto.setTipoMovimiento(movements.getTipoMovimiento());
            dto.setValorMovimiento(movements.getValor());
            dto.setSaldoMovimiento(movements.getSaldo());
            dto.setCreatedAt(movements.getCreatedAt());
            return dto;
        }).toList();
    }
}
