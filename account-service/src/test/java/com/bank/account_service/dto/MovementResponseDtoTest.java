package com.bank.account_service.dto;

import com.bank.account_service.entity.Movement.MovementType;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MovementResponseDtoTest {

    @Test
    void whenAllFieldsAreSet_thenGettersReturnCorrectValues() {
        MovementResponseDto dto = new MovementResponseDto();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);

        dto.setId(1L);
        dto.setCuentaId(123L);
        dto.setFecha(now);
        dto.setTipoMovimiento(MovementType.CREDITO);
        dto.setValor(BigDecimal.valueOf(100.00));
        dto.setSaldo(BigDecimal.valueOf(1100.00));
        dto.setDescripcion("Deposit");
        dto.setCreatedAt(createdAt);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getCuentaId()).isEqualTo(123L);
        assertThat(dto.getFecha()).isEqualTo(now);
        assertThat(dto.getTipoMovimiento()).isEqualTo(MovementType.CREDITO);
        assertThat(dto.getValor()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(dto.getSaldo()).isEqualByComparingTo(BigDecimal.valueOf(1100.00));
        assertThat(dto.getDescripcion()).isEqualTo("Deposit");
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void whenFieldsAreNull_thenGettersReturnNull() {
        MovementResponseDto dto = new MovementResponseDto();

        assertThat(dto.getId()).isNull();
        assertThat(dto.getCuentaId()).isNull();
        assertThat(dto.getFecha()).isNull();
        assertThat(dto.getTipoMovimiento()).isNull();
        assertThat(dto.getValor()).isNull();
        assertThat(dto.getSaldo()).isNull();
        assertThat(dto.getDescripcion()).isNull();
        assertThat(dto.getCreatedAt()).isNull();
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        
        MovementResponseDto dto1 = new MovementResponseDto();
        dto1.setId(1L);
        dto1.setCuentaId(123L);
        dto1.setFecha(now);
        dto1.setTipoMovimiento(MovementType.CREDITO);
        dto1.setValor(BigDecimal.valueOf(100.00));

        MovementResponseDto dto2 = new MovementResponseDto();
        dto2.setId(1L);
        dto2.setCuentaId(123L);
        dto2.setFecha(now);
        dto2.setTipoMovimiento(MovementType.CREDITO);
        dto2.setValor(BigDecimal.valueOf(100.00));

        MovementResponseDto dto3 = new MovementResponseDto();
        dto3.setId(2L);
        dto3.setCuentaId(456L);
        dto3.setTipoMovimiento(MovementType.DEBITO);

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1).isNotEqualTo(dto3);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        MovementResponseDto dto = new MovementResponseDto();
        dto.setId(1L);
        dto.setCuentaId(123L);
        dto.setTipoMovimiento(MovementType.CREDITO);
        dto.setValor(BigDecimal.valueOf(100.00));
        dto.setDescripcion("Deposit");

        String toString = dto.toString();
        
        assertThat(toString).contains("MovementResponseDto");
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("cuentaId=123");
        assertThat(toString).contains("tipoMovimiento=CREDITO");
        assertThat(toString).contains("valor=100");
        assertThat(toString).contains("descripcion=Deposit");
    }

    @Test
    void whenTestingBothMovementTypes_thenBothWork() {
        MovementResponseDto creditoDto = new MovementResponseDto();
        creditoDto.setTipoMovimiento(MovementType.CREDITO);

        MovementResponseDto debitoDto = new MovementResponseDto();
        debitoDto.setTipoMovimiento(MovementType.DEBITO);

        assertThat(creditoDto.getTipoMovimiento()).isEqualTo(MovementType.CREDITO);
        assertThat(debitoDto.getTipoMovimiento()).isEqualTo(MovementType.DEBITO);
    }

    @Test
    void whenDescripcionIsEmpty_thenGetterReturnsEmptyString() {
        MovementResponseDto dto = new MovementResponseDto();
        dto.setDescripcion("");

        assertThat(dto.getDescripcion()).isEmpty();
    }

    @Test
    void whenSaldoIsZero_thenGetterReturnsZero() {
        MovementResponseDto dto = new MovementResponseDto();
        dto.setSaldo(BigDecimal.ZERO);

        assertThat(dto.getSaldo()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void whenValorIsDecimal_thenPrecisionIsPreserved() {
        MovementResponseDto dto = new MovementResponseDto();
        BigDecimal preciseValue = new BigDecimal("123.456789");
        dto.setValor(preciseValue);

        assertThat(dto.getValor()).isEqualByComparingTo(preciseValue);
    }

    @Test
    void whenCreatingMovementForDebit_thenAllFieldsWork() {
        MovementResponseDto dto = new MovementResponseDto();
        LocalDateTime now = LocalDateTime.now();

        dto.setId(2L);
        dto.setCuentaId(456L);
        dto.setFecha(now);
        dto.setTipoMovimiento(MovementType.DEBITO);
        dto.setValor(BigDecimal.valueOf(50.00));
        dto.setSaldo(BigDecimal.valueOf(950.00));
        dto.setDescripcion("Withdrawal");
        dto.setCreatedAt(now);

        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getCuentaId()).isEqualTo(456L);
        assertThat(dto.getTipoMovimiento()).isEqualTo(MovementType.DEBITO);
        assertThat(dto.getValor()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
        assertThat(dto.getSaldo()).isEqualByComparingTo(BigDecimal.valueOf(950.00));
        assertThat(dto.getDescripcion()).isEqualTo("Withdrawal");
    }
}
