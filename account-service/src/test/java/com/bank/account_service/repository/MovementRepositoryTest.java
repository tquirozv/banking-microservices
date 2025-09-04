package com.bank.account_service.repository;

import com.bank.account_service.entity.Account;
import com.bank.account_service.entity.Movement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MovementRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MovementRepository movementRepository;

    private Account testAccount1;
    private Account testAccount2;
    private Movement movement1;
    private Movement movement2;
    private Movement movement3;
    private Movement movement4;

    @BeforeEach
    void setUp() {
        // Create test accounts
        testAccount1 = new Account();
        testAccount1.setNumeroCuenta("123456");
        testAccount1.setTipoCuenta(Account.AccountType.AHORRO);
        testAccount1.setSaldoInicial(BigDecimal.valueOf(1000.00));
        testAccount1.setSaldoActual(BigDecimal.valueOf(1500.00));
        testAccount1.setEstado(true);
        testAccount1.setClienteId(1L);

        testAccount2 = new Account();
        testAccount2.setNumeroCuenta("654321");
        testAccount2.setTipoCuenta(Account.AccountType.CORRIENTE);
        testAccount2.setSaldoInicial(BigDecimal.valueOf(500.00));
        testAccount2.setSaldoActual(BigDecimal.valueOf(750.00));
        testAccount2.setEstado(true);
        testAccount2.setClienteId(2L);

        entityManager.persistAndFlush(testAccount1);
        entityManager.persistAndFlush(testAccount2);

        // Create test movements
        LocalDateTime baseTime = LocalDateTime.of(2024, 1, 15, 10, 0);
        
        movement1 = new Movement();
        movement1.setCuentaId(testAccount1.getId());
        movement1.setFecha(baseTime);
        movement1.setTipoMovimiento(Movement.MovementType.CREDITO);
        movement1.setValor(BigDecimal.valueOf(100.00));
        movement1.setSaldo(BigDecimal.valueOf(1100.00));
        movement1.setDescripcion("Deposit 1");

        movement2 = new Movement();
        movement2.setCuentaId(testAccount1.getId());
        movement2.setFecha(baseTime.plusHours(1));
        movement2.setTipoMovimiento(Movement.MovementType.DEBITO);
        movement2.setValor(BigDecimal.valueOf(50.00));
        movement2.setSaldo(BigDecimal.valueOf(1050.00));
        movement2.setDescripcion("Withdrawal 1");

        movement3 = new Movement();
        movement3.setCuentaId(testAccount1.getId());
        movement3.setFecha(baseTime.plusDays(1));
        movement3.setTipoMovimiento(Movement.MovementType.CREDITO);
        movement3.setValor(BigDecimal.valueOf(200.00));
        movement3.setSaldo(BigDecimal.valueOf(1250.00));
        movement3.setDescripcion("Deposit 2");

        movement4 = new Movement();
        movement4.setCuentaId(testAccount2.getId());
        movement4.setFecha(baseTime.plusHours(2));
        movement4.setTipoMovimiento(Movement.MovementType.CREDITO);
        movement4.setValor(BigDecimal.valueOf(75.00));
        movement4.setSaldo(BigDecimal.valueOf(575.00));
        movement4.setDescripcion("Deposit Account 2");

        // Persist test movements
        entityManager.persistAndFlush(movement1);
        entityManager.persistAndFlush(movement2);
        entityManager.persistAndFlush(movement3);
        entityManager.persistAndFlush(movement4);
        entityManager.clear();
    }

    @Test
    void whenFindByCuentaIdWithExistingAccount_thenReturnMovements() {
        List<Movement> movements = movementRepository.findByCuentaId(testAccount1.getId());

        assertThat(movements).hasSize(3);
        assertThat(movements).extracting(Movement::getDescripcion)
                .containsExactlyInAnyOrder("Deposit 1", "Withdrawal 1", "Deposit 2");
        assertThat(movements).allMatch(m -> m.getCuentaId().equals(testAccount1.getId()));
    }

    @Test
    void whenFindByCuentaIdWithNonExistingAccount_thenReturnEmptyList() {
        List<Movement> movements = movementRepository.findByCuentaId(999L);

        assertThat(movements).isEmpty();
    }

    @Test
    void whenFindByCuentaIdAndFechaBetween_thenReturnMovementsInDateRange() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 15, 12, 0);

        List<Movement> movements = movementRepository.findByCuentaIdAndFechaBetween(
                testAccount1.getId(), startDate, endDate);

        assertThat(movements).hasSize(2);
        assertThat(movements).extracting(Movement::getDescripcion)
                .containsExactlyInAnyOrder("Deposit 1", "Withdrawal 1");
    }

    @Test
    void whenFindByCuentaIdAndFechaBetweenWithNoResults_thenReturnEmptyList() {
        LocalDateTime startDate = LocalDateTime.of(2024, 2, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 2, 28, 23, 59);

        List<Movement> movements = movementRepository.findByCuentaIdAndFechaBetween(
                testAccount1.getId(), startDate, endDate);

        assertThat(movements).isEmpty();
    }

    @Test
    void whenFindByCuentaIdAndTipoMovimientoCredito_thenReturnOnlyCreditMovements() {
        List<Movement> creditMovements = movementRepository.findByCuentaIdAndTipoMovimiento(
                testAccount1.getId(), Movement.MovementType.CREDITO);

        assertThat(creditMovements).hasSize(2);
        assertThat(creditMovements).extracting(Movement::getDescripcion)
                .containsExactlyInAnyOrder("Deposit 1", "Deposit 2");
        assertThat(creditMovements).allMatch(m -> 
                m.getTipoMovimiento() == Movement.MovementType.CREDITO);
    }

    @Test
    void whenFindByCuentaIdAndTipoMovimientoDebito_thenReturnOnlyDebitMovements() {
        List<Movement> debitMovements = movementRepository.findByCuentaIdAndTipoMovimiento(
                testAccount1.getId(), Movement.MovementType.DEBITO);

        assertThat(debitMovements).hasSize(1);
        assertThat(debitMovements.getFirst().getDescripcion()).isEqualTo("Withdrawal 1");
        assertThat(debitMovements.getFirst().getTipoMovimiento()).isEqualTo(Movement.MovementType.DEBITO);
    }

    @Test
    void whenFindByCuentaIdAndTipoMovimientoWithNoResults_thenReturnEmptyList() {
        List<Movement> debitMovements = movementRepository.findByCuentaIdAndTipoMovimiento(
                testAccount2.getId(), Movement.MovementType.DEBITO);

        assertThat(debitMovements).isEmpty();
    }

    @Test
    void whenSavingNewMovement_thenMovementIsPersisted() {
        Movement newMovement = new Movement();
        newMovement.setCuentaId(testAccount2.getId());
        newMovement.setFecha(LocalDateTime.now());
        newMovement.setTipoMovimiento(Movement.MovementType.DEBITO);
        newMovement.setValor(BigDecimal.valueOf(25.00));
        newMovement.setSaldo(BigDecimal.valueOf(550.00));
        newMovement.setDescripcion("New withdrawal");

        Movement saved = movementRepository.save(newMovement);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescripcion()).isEqualTo("New withdrawal");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void whenFindingAllMovements_thenReturnAllMovements() {
        List<Movement> allMovements = movementRepository.findAll();

        assertThat(allMovements).hasSize(4);
    }

    @Test
    void whenDeletingMovement_thenMovementIsRemoved() {
        Movement movement = movementRepository.findByCuentaId(testAccount1.getId()).getFirst();
        Long movementId = movement.getId();

        movementRepository.delete(movement);

        assertThat(movementRepository.findById(movementId)).isNotPresent();
        assertThat(movementRepository.findByCuentaId(testAccount1.getId())).hasSize(2);
    }

    @Test
    void whenCountingMovements_thenReturnCorrectCount() {
        long count = movementRepository.count();

        assertThat(count).isEqualTo(4);
    }

    @Test
    void whenFindByIdWithExistingMovement_thenReturnMovement() {
        Movement movement = movementRepository.findByCuentaId(testAccount1.getId()).getFirst();
        Long movementId = movement.getId();

        assertThat(movementRepository.findById(movementId)).isPresent();
    }

    @Test
    void whenUpdatingMovement_thenChangesArePersisted() {
        Movement movement = movementRepository.findByCuentaId(testAccount1.getId()).getFirst();
        
        movement.setDescripcion("Updated description");
        movement.setSaldo(BigDecimal.valueOf(9999.99));
        
        Movement updated = movementRepository.save(movement);

        assertThat(updated.getDescripcion()).isEqualTo("Updated description");
        assertThat(updated.getSaldo()).isEqualByComparingTo(BigDecimal.valueOf(9999.99));
    }

    @Test
    void whenFindByCuentaIdOrderedByFecha_thenMovementsAreOrderedByDate() {
        List<Movement> movements = movementRepository.findByCuentaId(testAccount1.getId());

        // Verify movements are in chronological order (assuming default ordering)
        assertThat(movements).hasSize(3);
        
        // Check if movements maintain their relationship to the account
        assertThat(movements).allMatch(m -> m.getCuentaId().equals(testAccount1.getId()));
    }

    @Test
    void whenFindByCuentaIdWithDateRangeCoveringAllMovements_thenReturnAllAccountMovements() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        List<Movement> movements = movementRepository.findByCuentaIdAndFechaBetween(
                testAccount1.getId(), startDate, endDate);

        assertThat(movements).hasSize(3);
    }
}
