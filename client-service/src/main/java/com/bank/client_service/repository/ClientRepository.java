package com.bank.client_service.repository;

import com.bank.client_service.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByPersonaIdentificacion(String identificacion);

    boolean existsByPersonaIdentificacion(String identificacion);

    List<Client> findByEstado(Boolean estado);

    List<Client> findByPersonaNombreContainingIgnoreCase(String nombre);

    List<Client> findByPersonaNombreStartingWithIgnoreCase(String prefix);

}
