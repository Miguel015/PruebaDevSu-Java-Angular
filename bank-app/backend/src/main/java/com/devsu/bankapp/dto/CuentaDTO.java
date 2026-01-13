package com.devsu.bankapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class CuentaDTO implements Serializable {
    private Long id;

    @NotBlank(message = "numeroCuenta es obligatorio")
    private String numeroCuenta;

    @NotBlank(message = "tipoCuenta es obligatorio")
    private String tipoCuenta;

    @NotNull(message = "saldoInicial es obligatorio")
    private BigDecimal saldoInicial;

    private Boolean estado;

    @NotNull(message = "clienteId es obligatorio")
    private Long clienteId;

    public CuentaDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}
