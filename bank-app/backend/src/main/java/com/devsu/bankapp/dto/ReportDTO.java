package com.devsu.bankapp.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ReportDTO implements Serializable {
    private ClienteDTO cliente;
    private List<CuentaDTO> cuentas;
    private BigDecimal totalDebitos;
    private BigDecimal totalCreditos;
    private Map<String, BigDecimal> saldos; // numeroCuenta -> saldo
    private String pdfBase64;

    public ReportDTO() {}

    public ClienteDTO getCliente() { return cliente; }
    public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }

    public List<CuentaDTO> getCuentas() { return cuentas; }
    public void setCuentas(List<CuentaDTO> cuentas) { this.cuentas = cuentas; }

    public BigDecimal getTotalDebitos() { return totalDebitos; }
    public void setTotalDebitos(BigDecimal totalDebitos) { this.totalDebitos = totalDebitos; }

    public BigDecimal getTotalCreditos() { return totalCreditos; }
    public void setTotalCreditos(BigDecimal totalCreditos) { this.totalCreditos = totalCreditos; }

    public Map<String, BigDecimal> getSaldos() { return saldos; }
    public void setSaldos(Map<String, BigDecimal> saldos) { this.saldos = saldos; }

    public String getPdfBase64() { return pdfBase64; }
    public void setPdfBase64(String pdfBase64) { this.pdfBase64 = pdfBase64; }
}
