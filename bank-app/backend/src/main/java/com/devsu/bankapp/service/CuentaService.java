package com.devsu.bankapp.service;

import com.devsu.bankapp.dto.CuentaDTO;

import java.util.List;

public interface CuentaService {
    CuentaDTO create(CuentaDTO dto);
    CuentaDTO update(Long id, CuentaDTO dto);
    CuentaDTO findById(Long id);
    List<CuentaDTO> findAll();
    void delete(Long id);
}
