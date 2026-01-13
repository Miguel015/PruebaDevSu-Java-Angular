package com.devsu.bankapp.service;

import com.devsu.bankapp.entity.Cuenta;
import com.devsu.bankapp.entity.Movimiento;
import com.devsu.bankapp.exception.BusinessException;
import com.devsu.bankapp.repository.CuentaRepository;
import com.devsu.bankapp.repository.MovimientoRepository;
import com.devsu.bankapp.service.impl.MovimientoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private MovimientoServiceImpl movimientoService;

    private Cuenta cuenta;

    @BeforeEach
    public void setup() {
        cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setSaldoInicial(BigDecimal.ZERO);
    }

    @Test
    public void cuandoSaldoEsCeroYSeHaceDebito_debeLanzarSaldoNoDisponible() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        com.devsu.bankapp.dto.MovimientoDTO dto = new com.devsu.bankapp.dto.MovimientoDTO();
        dto.setCuentaId(1L);
        dto.setTipoMovimiento("DEBITO");
        dto.setValor(new BigDecimal("100"));

        assertThrows(BusinessException.class, () -> movimientoService.create(dto));
    }

    @Test
    public void cuandoSeSuperaCupoDiario_debeLanzarCupoDiarioExcedido() {
        // cuenta con saldo suficiente
        cuenta.setSaldoInicial(new BigDecimal("2000"));
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        Movimiento m = new Movimiento();
        m.setValor(new BigDecimal("-900"));
        m.setFecha(LocalDateTime.now());

        when(movimientoRepository.findByCuentaIdAndFechaBetween(any(), any(), any())).thenReturn(Collections.singletonList(m));

        com.devsu.bankapp.dto.MovimientoDTO dto = new com.devsu.bankapp.dto.MovimientoDTO();
        dto.setCuentaId(1L);
        dto.setTipoMovimiento("DEBITO");
        dto.setValor(new BigDecimal("200"));

        assertThrows(BusinessException.class, () -> movimientoService.create(dto));
    }
}
