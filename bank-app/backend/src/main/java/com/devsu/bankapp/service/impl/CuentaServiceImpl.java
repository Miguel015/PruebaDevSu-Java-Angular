package com.devsu.bankapp.service.impl;

import com.devsu.bankapp.dto.CuentaDTO;
import com.devsu.bankapp.entity.Cliente;
import com.devsu.bankapp.entity.Cuenta;
import com.devsu.bankapp.exception.ResourceNotFoundException;
import com.devsu.bankapp.repository.ClienteRepository;
import com.devsu.bankapp.repository.CuentaRepository;
import com.devsu.bankapp.service.CuentaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public CuentaDTO create(CuentaDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId()).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        Cuenta c = new Cuenta();
        c.setNumeroCuenta(dto.getNumeroCuenta());
        c.setTipoCuenta(dto.getTipoCuenta());
        c.setSaldoInicial(dto.getSaldoInicial() != null ? dto.getSaldoInicial() : BigDecimal.ZERO);
        c.setEstado(dto.getEstado() != null ? dto.getEstado() : true);
        c.setCliente(cliente);
        cuentaRepository.save(c);
        dto.setId(c.getId());
        return dto;
    }

    @Override
    public CuentaDTO update(Long id, CuentaDTO dto) {
        Cuenta c = cuentaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        if (dto.getNumeroCuenta() != null) c.setNumeroCuenta(dto.getNumeroCuenta());
        if (dto.getTipoCuenta() != null) c.setTipoCuenta(dto.getTipoCuenta());
        if (dto.getSaldoInicial() != null) c.setSaldoInicial(dto.getSaldoInicial());
        if (dto.getEstado() != null) c.setEstado(dto.getEstado());
        cuentaRepository.save(c);
        dto.setId(c.getId());
        return dto;
    }

    @Override
    public CuentaDTO findById(Long id) {
        Cuenta c = cuentaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        CuentaDTO dto = new CuentaDTO();
        dto.setId(c.getId());
        dto.setNumeroCuenta(c.getNumeroCuenta());
        dto.setTipoCuenta(c.getTipoCuenta());
        dto.setSaldoInicial(c.getSaldoInicial());
        dto.setEstado(c.getEstado());
        dto.setClienteId(c.getCliente() != null ? c.getCliente().getId() : null);
        return dto;
    }

    @Override
    public List<CuentaDTO> findAll() {
        return cuentaRepository.findAll().stream().map(c -> {
            CuentaDTO dto = new CuentaDTO();
            dto.setId(c.getId());
            dto.setNumeroCuenta(c.getNumeroCuenta());
            dto.setTipoCuenta(c.getTipoCuenta());
            dto.setSaldoInicial(c.getSaldoInicial());
            dto.setEstado(c.getEstado());
            dto.setClienteId(c.getCliente() != null ? c.getCliente().getId() : null);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Cuenta c = cuentaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        cuentaRepository.delete(c);
    }
}
