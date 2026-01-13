package com.devsu.bankapp.service;

import com.devsu.bankapp.dto.ClienteDTO;

import java.util.List;

public interface ClienteService {
    ClienteDTO create(ClienteDTO dto);
    ClienteDTO update(Long id, ClienteDTO dto);
    ClienteDTO findById(Long id);
    List<ClienteDTO> findAll();
    void delete(Long id);
}
