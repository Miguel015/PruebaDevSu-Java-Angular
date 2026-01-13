package com.devsu.bankapp.repository;

import com.devsu.bankapp.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDateTime start, LocalDateTime end);
}
