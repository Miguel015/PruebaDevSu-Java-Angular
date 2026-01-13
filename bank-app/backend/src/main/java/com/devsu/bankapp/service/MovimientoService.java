package com.devsu.bankapp.service;

import com.devsu.bankapp.dto.MovimientoDTO;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoService {
    MovimientoDTO create(MovimientoDTO dto);
    MovimientoDTO update(Long id, MovimientoDTO dto);
    MovimientoDTO findById(Long id);
    List<MovimientoDTO> findAll();
    void delete(Long id);
    List<MovimientoDTO> findByCuentaAndRange(Long cuentaId, java.time.LocalDateTime start, java.time.LocalDateTime end);
}
