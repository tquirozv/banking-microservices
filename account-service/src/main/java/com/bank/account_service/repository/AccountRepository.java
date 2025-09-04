package com.bank.account_service.repository;

import com.bank.account_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNumeroCuenta(String numeroCuenta);
    List<Account> findByClienteId(Long clienteId);
    List<Account> findByEstado(Boolean estado);
}
