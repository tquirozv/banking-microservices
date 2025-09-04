package com.bank.account_service.repository;

import com.bank.account_service.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    private Account testAccount1;
    private Account testAccount2;
    private Account testAccount3;

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
        testAccount2.setEstado(false);
        testAccount2.setClienteId(2L);

        testAccount3 = new Account();
        testAccount3.setNumeroCuenta("789012");
        testAccount3.setTipoCuenta(Account.AccountType.AHORRO);
        testAccount3.setSaldoInicial(BigDecimal.valueOf(2000.00));
        testAccount3.setSaldoActual(BigDecimal.valueOf(2200.00));
        testAccount3.setEstado(true);
        testAccount3.setClienteId(1L); // Same client as testAccount1

        // Persist test data
        entityManager.persistAndFlush(testAccount1);
        entityManager.persistAndFlush(testAccount2);
        entityManager.persistAndFlush(testAccount3);
        entityManager.clear();
    }

    @Test
    void whenFindByNumeroCuentaWithExistingAccount_thenReturnAccount() {
        Optional<Account> found = accountRepository.findByNumeroCuenta("123456");

        assertThat(found).isPresent();
        assertThat(found.get().getNumeroCuenta()).isEqualTo("123456");
        assertThat(found.get().getTipoCuenta()).isEqualTo(Account.AccountType.AHORRO);
        assertThat(found.get().getClienteId()).isEqualTo(1L);
        assertThat(found.get().getEstado()).isTrue();
    }

    @Test
    void whenFindByNumeroCuentaWithNonExistingAccount_thenReturnEmpty() {
        Optional<Account> found = accountRepository.findByNumeroCuenta("999999");

        assertThat(found).isNotPresent();
    }

    @Test
    void whenFindByNumeroCuentaWithNullParameter_thenReturnEmpty() {
        Optional<Account> found = accountRepository.findByNumeroCuenta(null);

        assertThat(found).isNotPresent();
    }

    @Test
    void whenFindByClienteIdWithExistingClient_thenReturnAccountsList() {
        List<Account> accounts = accountRepository.findByClienteId(1L);

        assertThat(accounts).hasSize(2);
        assertThat(accounts).extracting(Account::getNumeroCuenta)
                .containsExactlyInAnyOrder("123456", "789012");
        assertThat(accounts).extracting(Account::getClienteId)
                .containsOnly(1L);
    }

    @Test
    void whenFindByClienteIdWithNonExistingClient_thenReturnEmptyList() {
        List<Account> accounts = accountRepository.findByClienteId(999L);

        assertThat(accounts).isEmpty();
    }

    @Test
    void whenFindByClienteIdWithSingleAccount_thenReturnSingleAccountList() {
        List<Account> accounts = accountRepository.findByClienteId(2L);

        assertThat(accounts).hasSize(1);
        assertThat(accounts.getFirst().getNumeroCuenta()).isEqualTo("654321");
        assertThat(accounts.getFirst().getClienteId()).isEqualTo(2L);
        assertThat(accounts.getFirst().getTipoCuenta()).isEqualTo(Account.AccountType.CORRIENTE);
    }

    @Test
    void whenFindByEstadoTrue_thenReturnActiveAccounts() {
        List<Account> activeAccounts = accountRepository.findByEstado(true);

        assertThat(activeAccounts).hasSize(2);
        assertThat(activeAccounts).extracting(Account::getNumeroCuenta)
                .containsExactlyInAnyOrder("123456", "789012");
        assertThat(activeAccounts).extracting(Account::getEstado)
                .containsOnly(true);
    }

    @Test
    void whenFindByEstadoFalse_thenReturnInactiveAccounts() {
        List<Account> inactiveAccounts = accountRepository.findByEstado(false);

        assertThat(inactiveAccounts).hasSize(1);
        assertThat(inactiveAccounts.getFirst().getNumeroCuenta()).isEqualTo("654321");
        assertThat(inactiveAccounts.getFirst().getEstado()).isFalse();
    }

    @Test
    void whenFindByEstadoWithNullParameter_thenReturnEmptyList() {
        List<Account> accounts = accountRepository.findByEstado(null);

        assertThat(accounts).isEmpty();
    }

    @Test
    void whenSavingNewAccount_thenAccountIsPersisted() {
        Account newAccount = new Account();
        newAccount.setNumeroCuenta("NEW123");
        newAccount.setTipoCuenta(Account.AccountType.CORRIENTE);
        newAccount.setSaldoInicial(BigDecimal.valueOf(300.00));
        newAccount.setSaldoActual(BigDecimal.valueOf(300.00));
        newAccount.setEstado(true);
        newAccount.setClienteId(3L);

        Account saved = accountRepository.save(newAccount);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNumeroCuenta()).isEqualTo("NEW123");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void whenUpdatingExistingAccount_thenChangesArePersisted() {
        Account account = accountRepository.findByNumeroCuenta("123456").orElseThrow();
        
        account.setSaldoActual(BigDecimal.valueOf(2000.00));
        account.setEstado(false);
        
        Account updated = accountRepository.save(account);

        assertThat(updated.getSaldoActual()).isEqualByComparingTo(BigDecimal.valueOf(2000.00));
        assertThat(updated.getEstado()).isFalse();
        assertThat(updated.getUpdatedAt()).isNotNull();
    }

    @Test
    void whenDeletingAccount_thenAccountIsRemoved() {
        Account account = accountRepository.findByNumeroCuenta("123456").orElseThrow();
        Long accountId = account.getId();

        accountRepository.delete(account);

        Optional<Account> deleted = accountRepository.findById(accountId);
        assertThat(deleted).isNotPresent();
    }

    @Test
    void whenFindingAll_thenReturnAllAccounts() {
        List<Account> allAccounts = accountRepository.findAll();

        assertThat(allAccounts).hasSize(3);
        assertThat(allAccounts).extracting(Account::getNumeroCuenta)
                .containsExactlyInAnyOrder("123456", "654321", "789012");
    }

    @Test
    void whenFindById_thenReturnCorrectAccount() {
        Account account = accountRepository.findByNumeroCuenta("123456").orElseThrow();
        Long accountId = account.getId();

        Optional<Account> found = accountRepository.findById(accountId);

        assertThat(found).isPresent();
        assertThat(found.get().getNumeroCuenta()).isEqualTo("123456");
    }

    @Test
    void whenCountingAccounts_thenReturnCorrectCount() {
        long count = accountRepository.count();

        assertThat(count).isEqualTo(3);
    }

    @Test
    void whenCheckingExistence_thenReturnCorrectStatus() {
        Account account = accountRepository.findByNumeroCuenta("123456").orElseThrow();
        Long accountId = account.getId();

        boolean exists = accountRepository.existsById(accountId);
        boolean notExists = accountRepository.existsById(999L);

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
