package com.devsu.bankapp.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoDTO implements Serializable {
    private Long id;

    private LocalDateTime fecha;

    @NotBlank(message = "tipoMovimiento es obligatorio")
    @JsonAlias({"tipo", "tipoMovimiento"})
    private String tipoMovimiento;

    @NotNull(message = "valor es obligatorio")
    @DecimalMin(value = "0.01", message = "valor debe ser mayor que 0")
    private BigDecimal valor;

    private BigDecimal saldo;

    @NotNull(message = "cuentaId es obligatorio")
    private Long cuentaId;

    public MovimientoDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }
}
