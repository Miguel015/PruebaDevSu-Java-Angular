package com.devsu.bankapp.repository;

import com.devsu.bankapp.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    java.util.List<Cuenta> findByClienteId(Long clienteId);
}
