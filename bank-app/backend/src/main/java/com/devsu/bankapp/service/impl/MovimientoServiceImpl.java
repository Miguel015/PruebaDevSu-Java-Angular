package com.devsu.bankapp.service.impl;

import com.devsu.bankapp.dto.MovimientoDTO;
import com.devsu.bankapp.entity.Cuenta;
import com.devsu.bankapp.entity.Movimiento;
import com.devsu.bankapp.exception.BusinessException;
import com.devsu.bankapp.exception.ResourceNotFoundException;
import com.devsu.bankapp.repository.CuentaRepository;
import com.devsu.bankapp.repository.MovimientoRepository;
import com.devsu.bankapp.service.MovimientoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private static final BigDecimal LIMITE_DIARIO = new BigDecimal("1000");

    public MovimientoServiceImpl(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    @Transactional
    public MovimientoDTO create(MovimientoDTO dto) {
        Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId()).orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        String tipo = dto.getTipoMovimiento() != null ? dto.getTipoMovimiento().trim().toUpperCase() : "";
        BigDecimal inputValor = dto.getValor();
        if (inputValor == null || inputValor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El valor del movimiento debe ser positivo");
        }

        BigDecimal storedValor;
        BigDecimal currentBalance = cuenta.getSaldoInicial() != null ? cuenta.getSaldoInicial() : BigDecimal.ZERO;
        BigDecimal newBalance;

        if ("CREDITO".equals(tipo)) {
            storedValor = inputValor;
            newBalance = currentBalance.add(storedValor);
        } else if ("DEBITO".equals(tipo) || "DEBITO".equals(tipo)) {
            // débito: stored as negativo
            if (currentBalance.compareTo(BigDecimal.ZERO) == 0) {
                throw new BusinessException("Saldo no disponible");
            }

            // calcular cupo diario
            LocalDate today = LocalDate.now();
            LocalDateTime start = today.atStartOfDay();
            LocalDateTime end = today.atTime(LocalTime.MAX);
            List<Movimiento> movimientosHoy = movimientoRepository.findByCuentaIdAndFechaBetween(cuenta.getId(), start, end);
            BigDecimal totalDebitosHoy = movimientosHoy.stream()
                    .filter(m -> m.getValor().compareTo(BigDecimal.ZERO) < 0)
                    .map(m -> m.getValor().abs())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal requested = inputValor; // user provides positive amount to withdraw
            if (totalDebitosHoy.add(requested).compareTo(LIMITE_DIARIO) > 0) {
                throw new BusinessException("Cupo diario Excedido");
            }

            storedValor = inputValor.negate();
            newBalance = currentBalance.add(storedValor);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Saldo no disponible");
            }
        } else {
            throw new BusinessException("Tipo de movimiento inválido");
        }

        Movimiento m = new Movimiento();
        m.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDateTime.now());
        m.setTipoMovimiento(tipo);
        m.setValor(storedValor);
        m.setSaldo(newBalance);
        m.setCuenta(cuenta);

        movimientoRepository.save(m);

        // actualizar saldo en cuenta
        cuenta.setSaldoInicial(newBalance);
        cuentaRepository.save(cuenta);

        MovimientoDTO out = new MovimientoDTO();
        out.setId(m.getId());
        out.setFecha(m.getFecha());
        out.setTipoMovimiento(m.getTipoMovimiento());
        out.setValor(m.getValor());
        out.setSaldo(m.getSaldo());
        out.setCuentaId(cuenta.getId());
        return out;
    }

    @Override
    public MovimientoDTO findById(Long id) {
        Movimiento m = movimientoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(m.getId());
        dto.setFecha(m.getFecha());
        dto.setTipoMovimiento(m.getTipoMovimiento());
        dto.setValor(m.getValor());
        dto.setSaldo(m.getSaldo());
        dto.setCuentaId(m.getCuenta() != null ? m.getCuenta().getId() : null);
        return dto;
    }

    @Override
    public List<MovimientoDTO> findAll() {
        return movimientoRepository.findAll().stream().map(m -> {
            MovimientoDTO dto = new MovimientoDTO();
            dto.setId(m.getId());
            dto.setFecha(m.getFecha());
            dto.setTipoMovimiento(m.getTipoMovimiento());
            dto.setValor(m.getValor());
            dto.setSaldo(m.getSaldo());
            dto.setCuentaId(m.getCuenta() != null ? m.getCuenta().getId() : null);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Movimiento m = movimientoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));
        movimientoRepository.delete(m);
    }

    @Override
    @Transactional
    public MovimientoDTO update(Long id, MovimientoDTO dto) {
        Movimiento m = movimientoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));
        Cuenta cuenta = m.getCuenta();

        if (dto.getCuentaId() != null && !dto.getCuentaId().equals(cuenta.getId())) {
            throw new BusinessException("No se permite cambiar la cuenta de un movimiento existente");
        }

        String newTipo = dto.getTipoMovimiento() != null ? dto.getTipoMovimiento().trim().toUpperCase() : "";
        BigDecimal inputValor = dto.getValor();
        if (inputValor == null || inputValor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El valor del movimiento debe ser positivo");
        }

        BigDecimal oldStored = m.getValor();
        BigDecimal newStored;
        BigDecimal currentBalance = cuenta.getSaldoInicial() != null ? cuenta.getSaldoInicial() : BigDecimal.ZERO;

        if ("CREDITO".equals(newTipo)) {
            newStored = inputValor;
        } else if ("DEBITO".equals(newTipo)) {
            // check daily limit excluding this movement
            LocalDate today = m.getFecha().toLocalDate();
            LocalDateTime start = today.atStartOfDay();
            LocalDateTime end = today.atTime(LocalTime.MAX);
            List<Movimiento> movimientosHoy = movimientoRepository.findByCuentaIdAndFechaBetween(cuenta.getId(), start, end);
            BigDecimal totalDebitosHoy = movimientosHoy.stream()
                    .filter(x -> x.getValor().compareTo(BigDecimal.ZERO) < 0 && !x.getId().equals(m.getId()))
                    .map(x -> x.getValor().abs())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal requested = inputValor;
            if (totalDebitosHoy.add(requested).compareTo(LIMITE_DIARIO) > 0) {
                throw new BusinessException("Cupo diario Excedido");
            }
            newStored = inputValor.negate();
        } else {
            throw new BusinessException("Tipo de movimiento inválido");
        }

        BigDecimal delta = newStored.subtract(oldStored);
        BigDecimal newBalance = currentBalance.add(delta);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Saldo no disponible");
        }

        m.setTipoMovimiento(newTipo);
        m.setValor(newStored);
        m.setSaldo(newBalance);
        movimientoRepository.save(m);

        cuenta.setSaldoInicial(newBalance);
        cuentaRepository.save(cuenta);

        MovimientoDTO out = new MovimientoDTO();
        out.setId(m.getId());
        out.setFecha(m.getFecha());
        out.setTipoMovimiento(m.getTipoMovimiento());
        out.setValor(m.getValor());
        out.setSaldo(m.getSaldo());
        out.setCuentaId(cuenta.getId());
        return out;
    }

    @Override
    public List<MovimientoDTO> findByCuentaAndRange(Long cuentaId, LocalDateTime start, LocalDateTime end) {
        return movimientoRepository.findByCuentaIdAndFechaBetween(cuentaId, start, end).stream().map(m -> {
            MovimientoDTO dto = new MovimientoDTO();
            dto.setId(m.getId());
            dto.setFecha(m.getFecha());
            dto.setTipoMovimiento(m.getTipoMovimiento());
            dto.setValor(m.getValor());
            dto.setSaldo(m.getSaldo());
            dto.setCuentaId(m.getCuenta() != null ? m.getCuenta().getId() : null);
            return dto;
        }).collect(Collectors.toList());
    }
}
